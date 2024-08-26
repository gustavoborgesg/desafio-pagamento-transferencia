package com.pagamento.desafio.pagamento_simplificado.domain.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException exception) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(exception.getMessage(), exception.getErrorCode());
        return new ResponseEntity<>(customErrorResponse, exception.getHttpStatus());
    }
}
