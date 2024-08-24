package com.pagamento.desafio.pagamento_simplificado.controllers.dtos;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
}