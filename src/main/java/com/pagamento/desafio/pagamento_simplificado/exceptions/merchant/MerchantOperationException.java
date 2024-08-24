package com.pagamento.desafio.pagamento_simplificado.exceptions.merchant;

import com.pagamento.desafio.pagamento_simplificado.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class MerchantOperationException extends CustomException {
    public MerchantOperationException(String message) {
        super(message, "MERCHANT_OPERATION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
