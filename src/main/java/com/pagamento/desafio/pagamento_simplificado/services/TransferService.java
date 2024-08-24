package com.pagamento.desafio.pagamento_simplificado.services;


import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.entities.Transfer;

import java.util.List;

public interface TransferService {
    Transfer executeTransfer(TransferRequest transferRequest);

    Transfer getTransferById(Long id);

    List<Transfer> getAllTransfers();
}
