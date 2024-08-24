package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin;

import lombok.Data;

@Data
public class AdminRegistrationRequest {
    private String username;
    private String email;
    private String password;
}
