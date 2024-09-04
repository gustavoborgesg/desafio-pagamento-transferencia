package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.InsufficientBalanceException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferAuthorizationException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotAllowedException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.TransferService;
import com.pagamento.desafio.pagamento_simplificado.external.dtos.AuthorizationResponse;
import com.pagamento.desafio.pagamento_simplificado.external.dtos.NotificationRequest;
import com.pagamento.desafio.pagamento_simplificado.external.exceptions.authorization.AuthorizationServiceException;
import com.pagamento.desafio.pagamento_simplificado.external.exceptions.notification.NotificationServiceException;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final UserAccountRepository userAccountRepository;
    private final RestClient restClient = RestClient.create();
    private final String AUTHORIZATION_URL = "https://util.devi.tools/api/v2/authorize";
    private final String NOTIFICATION_URL = "https://util.devi.tools/api/v1/notify";

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Override
    @Transactional
    public Transfer executeTransfer(TransferRequest transferRequest) {
        UserAccount payer = userAccountRepository.findById(transferRequest.getPayer())
                .orElseThrow(() -> new TransferNotFoundException("Payer not found"));

        UserAccount payee = userAccountRepository.findById(transferRequest.getPayee())
                .orElseThrow(() -> new TransferNotFoundException("Payee not found"));

        // Only Clients can transfer to other Clients or Merchants
        if (payer.getRole().equals("ROLE_MERCHANT")) {
            throw new TransferNotAllowedException("Merchants cannot send money");
        }

        BigDecimal amount = transferRequest.getValue();

        if (!payer.getWallet().hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Payer has insufficient balance");
        }

        // Calling external authorization service
        authorizeTransfer();

        payer.getWallet().debit(amount);
        payee.getWallet().credit(amount);

        Transfer transfer = new Transfer();
        transfer.setPayer(payer);
        transfer.setPayee(payee);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setStatus("COMPLETED");

        notifyPayee(transfer);

        return transferRepository.save(transfer);
    }

    private void authorizeTransfer() {
        try {
            restClient.get()
                    .uri(AUTHORIZATION_URL)
                    .retrieve()
                    .body(AuthorizationResponse.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new TransferAuthorizationException("Transfer not authorized");
            }
        } catch (Exception e) {
            throw new AuthorizationServiceException("Failed to receive a valid response from the authorization service", e);
        }
    }

    protected void notifyPayee(Transfer transfer) {
        try {
            String message = String.format(
                    "Received %s from %s at %s",
                    transfer.getAmount(),
                    transfer.getPayer().getName(),
                    transfer.getTimestamp()
            );
            NotificationRequest notificationRequest = new NotificationRequest(transfer.getPayee().getEmail(), message);

            restClient.post()
                    .uri(NOTIFICATION_URL)
                    .body(notificationRequest)
                    .retrieve()
                    .toEntity(Void.class);

        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
                //throw new NotificationServiceException("Notification service is not available, try again later", e);
                logger.warn("Notification service is not available, try again later: {}", e.getMessage());
            }
        } catch (Exception e) {
            throw new NotificationServiceException("Failed to send notification to payee", e);
        }
    }

    @Override
    public Transfer getTransferById(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transfer not found with id " + id));
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
