package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientRegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String cpf;
    private BigDecimal initialBalance;
}
