package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class AdminOperationException extends CustomException {
    public AdminOperationException(String message) {
        super(message, "ADMIN_OPERATION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
