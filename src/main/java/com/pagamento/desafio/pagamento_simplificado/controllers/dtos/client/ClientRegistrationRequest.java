package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client;

import lombok.Data;

@Data
public class ClientRegistrationRequest {
    private String cpf;
    private String name;
    private String email;
    private String password;
}
