package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import lombok.Data;

@Data
public class MerchantRegistrationRequest {
    private String name;
    private String cnpj;
    private String email;
    private String password;
}
