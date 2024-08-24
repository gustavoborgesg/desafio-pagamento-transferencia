package com.pagamento.desafio.pagamento_simplificado.controllers.dtos;

import lombok.Data;

@Data
public class MerchantRegistrationRequest {
    private String cnpj;
    private String email;
    private String name;
    private String password;
}
