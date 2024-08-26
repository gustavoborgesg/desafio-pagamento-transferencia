package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends CustomException {
    public AdminNotFoundException(String message) {
        super(message, "ADMIN_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
