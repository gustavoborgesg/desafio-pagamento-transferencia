package com.pagamento.desafio.pagamento_simplificado.external.exceptions.authorization;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class AuthorizationServiceException extends CustomException {
    public AuthorizationServiceException(String message, Throwable cause) {
        super(message, cause, "TRANSFER_SERVICE_ERROR", HttpStatus.FORBIDDEN);
    }
}
