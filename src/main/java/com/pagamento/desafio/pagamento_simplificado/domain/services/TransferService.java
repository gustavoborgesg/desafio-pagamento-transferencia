package com.pagamento.desafio.pagamento_simplificado.domain.services;


import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;

import java.util.List;

public interface TransferService {
    Transfer executeTransfer(TransferRequest transferRequest);

    Transfer getTransferById(Long id);

    List<Transfer> getAllTransfers();
}
