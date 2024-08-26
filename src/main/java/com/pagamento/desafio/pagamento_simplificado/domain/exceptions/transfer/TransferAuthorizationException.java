package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class TransferAuthorizationException extends CustomException {
    public TransferAuthorizationException(String message) {
        super(message, "TRANSFER_UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
}
