package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class ClientAlreadyExistsException extends CustomException {
    public ClientAlreadyExistsException(String message) {
        super(message, "CLIENT_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
