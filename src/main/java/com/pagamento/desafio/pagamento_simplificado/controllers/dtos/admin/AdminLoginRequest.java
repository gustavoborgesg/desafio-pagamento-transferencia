package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String username;
    private String password;
}
