package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.entities.SystemUser;
import com.pagamento.desafio.pagamento_simplificado.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.InsufficientBalanceException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.transfer.TransferNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.repositories.SystemUserRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.services.TransferService;
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
    private final SystemUserRepository systemUserRepository;

    @Override
    @Transactional
    public Transfer executeTransfer(TransferRequest transferRequest) {
        SystemUser payer = systemUserRepository.findById(transferRequest.getPayerId())
                .orElseThrow(() -> new TransferNotFoundException("Payer not found"));

        SystemUser payee = systemUserRepository.findById(transferRequest.getPayeeId())
                .orElseThrow(() -> new TransferNotFoundException("Payee not found"));

        BigDecimal amount = transferRequest.getAmount();

        if (!payer.getWallet().hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Payer has insufficient balance");
        }

        payer.getWallet().debit(amount);
        payee.getWallet().credit(amount);

        Transfer transfer = new Transfer();
        transfer.setPayer(payer);
        transfer.setPayee(payee);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setStatus("COMPLETED");

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
