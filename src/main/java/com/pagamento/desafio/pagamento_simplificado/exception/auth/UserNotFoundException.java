package com.pagamento.desafio.pagamento_simplificado.exception.auth;

import com.pagamento.desafio.pagamento_simplificado.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
