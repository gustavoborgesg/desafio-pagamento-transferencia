package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import lombok.Data;

@Data
public class MerchantUpdateRequest {
    private String name;
    private String email;
    private String password;
}
