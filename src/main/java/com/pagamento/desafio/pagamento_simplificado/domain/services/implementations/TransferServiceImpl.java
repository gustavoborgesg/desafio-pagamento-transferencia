package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.InsufficientBalanceException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotAllowedException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.TransferService;
import com.pagamento.desafio.pagamento_simplificado.external.dtos.NotificationRequest;
import com.pagamento.desafio.pagamento_simplificado.external.services.TransferRestClient;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final UserAccountRepository userAccountRepository;
    private final TransferRestClient transferRestClient;

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
        transferRestClient.authorizeTransfer();

        payer.getWallet().debit(amount);
        payee.getWallet().credit(amount);

        Transfer transfer = new Transfer();
        transfer.setPayer(payer);
        transfer.setPayee(payee);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setStatus("COMPLETED");

        NotificationRequest notificationRequest = new NotificationRequest(
                transfer.getPayee().getEmail(),
                String.format("Received %s from %s at %s", transfer.getAmount(), transfer.getPayer().getName(), transfer.getTimestamp())
        );
        transferRestClient.notifyPayee(notificationRequest);

        return transferRepository.save(transfer);
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
