package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends CustomException {
    public ClientNotFoundException(String message) {
        super(message, "CLIENT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
