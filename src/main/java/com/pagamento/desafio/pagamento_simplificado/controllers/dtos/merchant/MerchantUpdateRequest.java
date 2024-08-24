package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import lombok.Data;

@Data
public class MerchantUpdateRequest {
    private String email;
    private String name;
    private String password;
}
