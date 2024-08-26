package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantDefaultResponse {
    private Long id;
    private String email;
    private String name;
    private String cnpj;
    private BigDecimal balance;
}
