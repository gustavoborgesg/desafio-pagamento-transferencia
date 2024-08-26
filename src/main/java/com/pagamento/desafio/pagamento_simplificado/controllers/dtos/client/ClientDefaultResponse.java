package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientDefaultResponse {
    private Long id;
    private String email;
    private String name;
    private String cpf;
    private BigDecimal balance;
}
