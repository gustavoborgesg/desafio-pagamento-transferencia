package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class AdminAlreadyExistsException extends CustomException {
    public AdminAlreadyExistsException(String message) {
        super(message, "ADMIN_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
