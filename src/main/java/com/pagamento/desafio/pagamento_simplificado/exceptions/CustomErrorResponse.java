package com.pagamento.desafio.pagamento_simplificado.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomErrorResponse {
    private final String message;
    private final String errorCode;
    private final LocalDateTime timestamp;

    public CustomErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}
