package com.pagamento.desafio.pagamento_simplificado.exception.auth;

import com.pagamento.desafio.pagamento_simplificado.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
