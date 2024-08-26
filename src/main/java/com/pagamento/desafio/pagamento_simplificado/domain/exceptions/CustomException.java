package com.pagamento.desafio.pagamento_simplificado.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public CustomException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
