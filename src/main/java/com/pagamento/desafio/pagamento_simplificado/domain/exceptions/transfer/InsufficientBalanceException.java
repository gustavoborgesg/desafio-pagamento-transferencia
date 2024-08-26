package com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends CustomException {
    public InsufficientBalanceException(String message) {
        super(message, "INSUFFICIENT_BALANCE", HttpStatus.BAD_REQUEST);
    }
}
