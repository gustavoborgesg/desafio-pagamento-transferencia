package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.user;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class UserAccountAlreadyExistsException extends CustomException {
    public UserAccountAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
