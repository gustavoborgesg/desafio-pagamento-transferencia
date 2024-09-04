package com.pagamento.desafio.pagamento_simplificado.domain.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CustomValidationErrorResponse extends CustomErrorResponse {

    private Map<String, String> validationErrors;

    public CustomValidationErrorResponse(String message, String errorCode) {
        super(message, errorCode);
    }
}
