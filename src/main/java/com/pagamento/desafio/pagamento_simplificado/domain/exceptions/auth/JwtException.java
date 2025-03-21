package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.auth;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class JwtException extends CustomException {
    public JwtException(String message) {
        super(message, "JWT_ERROR", HttpStatus.UNAUTHORIZED);
    }
}
