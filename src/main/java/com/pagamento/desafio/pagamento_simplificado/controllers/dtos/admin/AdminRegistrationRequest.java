package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin;

import lombok.Data;

@Data
public class AdminRegistrationRequest {
    private String cpf;
    private String name;
    private String email;
    private String password;
}
