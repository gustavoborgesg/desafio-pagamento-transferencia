package com.pagamento.desafio.pagamento_simplificado.exceptions.merchant;

import com.pagamento.desafio.pagamento_simplificado.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class MerchantAlreadyExistsException extends CustomException {
    public MerchantAlreadyExistsException(String message) {
        super(message, "MERCHANT_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
