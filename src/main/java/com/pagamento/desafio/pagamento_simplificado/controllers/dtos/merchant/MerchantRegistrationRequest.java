package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantRegistrationRequest {
    private String cnpj;
    private String name;
    private String email;
    private String password;
    private BigDecimal initialBalance;
}
