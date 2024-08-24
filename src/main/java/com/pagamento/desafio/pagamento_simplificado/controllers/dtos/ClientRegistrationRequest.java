package com.pagamento.desafio.pagamento_simplificado.controllers.dtos;

import lombok.Data;

@Data
public class ClientRegistrationRequest {
    private String cpf;
    private String email;
    private String name;
    private String password;
}
