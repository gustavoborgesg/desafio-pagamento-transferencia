package com.pagamento.desafio.pagamento_simplificado.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(customErrorResponse, ex.getHttpStatus());
    }
}
