package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.entities.SystemUser;
import com.pagamento.desafio.pagamento_simplificado.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.InsufficientBalanceException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.TransferAuthorizationException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.TransferNotAllowedException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.TransferNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.repositories.SystemUserRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final SystemUserRepository systemUserRepository;
    private final RestClient restClient = RestClient.create();

    @Override
    @Transactional
    public Transfer executeTransfer(TransferRequest transferRequest) {
        SystemUser payer = systemUserRepository.findById(transferRequest.getPayerId())
                .orElseThrow(() -> new TransferNotFoundException("Payer not found"));

        SystemUser payee = systemUserRepository.findById(transferRequest.getPayeeId())
                .orElseThrow(() -> new TransferNotFoundException("Payee not found"));

        // Lojistas não podem enviar dinheiro
        if (payer.getRole().equals("ROLE_MERCHANT")) {
            throw new TransferNotAllowedException("Merchants cannot send money");
        }

        BigDecimal amount = transferRequest.getValue();

        if (!payer.getWallet().hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Payer has insufficient balance");
        }

        // Chamada ao serviço de autorização externa
        String authorizationUrl = "https://util.devi.tools/api/v2/authorize";
        Boolean isAuthorized = restClient.get().uri(authorizationUrl).retrieve().body(Boolean.class);

        if (Boolean.FALSE.equals(isAuthorized)) {
            throw new TransferAuthorizationException("Transfer not authorized");
        }

        payer.getWallet().debit(amount);
        payee.getWallet().credit(amount);

        Transfer transfer = new Transfer();
        transfer.setPayer(payer);
        transfer.setPayee(payee);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setStatus("COMPLETED");

        notifyPayee(payee);

        return transferRepository.save(transfer);
    }

    private void notifyPayee(SystemUser payee) {
        String notifyUrl = "https://util.devi.tools/api/v1/notify";
        try {
            restClient.post().uri(notifyUrl).body(Void.class);
        } catch (Exception e) {
            e.printStackTrace();
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
