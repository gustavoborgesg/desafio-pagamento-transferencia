package com.pagamento.desafio.pagamento_simplificado.exceptions.transfer;

import com.pagamento.desafio.pagamento_simplificado.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class TransferNotFoundException extends CustomException {
    public TransferNotFoundException(String message) {
        super(message, "TRANSFER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
