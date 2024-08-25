package com.pagamento.desafio.pagamento_simplificado.exceptions.transfer;

import com.pagamento.desafio.pagamento_simplificado.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class TransferNotAllowedException extends CustomException {
    public TransferNotAllowedException(String message) {
        super(message, "TRANSFER_NOT_ALLOWED", HttpStatus.FORBIDDEN);
    }
}
