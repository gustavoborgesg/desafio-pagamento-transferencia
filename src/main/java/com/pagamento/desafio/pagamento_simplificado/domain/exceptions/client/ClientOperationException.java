package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class ClientOperationException extends CustomException {
    public ClientOperationException(String message) {
        super(message, "CLIENT_OPERATION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
