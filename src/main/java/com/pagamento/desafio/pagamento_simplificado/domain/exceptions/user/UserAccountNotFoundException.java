package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.user;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class UserAccountNotFoundException extends CustomException {
    public UserAccountNotFoundException(String message) {
        super(message, "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
