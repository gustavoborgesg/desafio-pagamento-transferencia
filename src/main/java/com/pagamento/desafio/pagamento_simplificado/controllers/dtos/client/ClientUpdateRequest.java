package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client;

import lombok.Data;

@Data
public class ClientUpdateRequest {
    private String email;
    private String name;
    private String password;
}
