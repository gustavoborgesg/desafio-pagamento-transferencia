package com.pagamento.desafio.pagamento_simplificado.exceptions.merchant;

import com.pagamento.desafio.pagamento_simplificado.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class MerchantNotFoundException extends CustomException {
    public MerchantNotFoundException(String message) {
        super(message, "MERCHANT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
