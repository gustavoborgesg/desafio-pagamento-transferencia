package com.pagamento.desafio.pagamento_simplificado.controllers.dtos;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}