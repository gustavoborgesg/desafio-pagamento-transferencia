package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.auth;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends CustomException {
    public InvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
    }
}
