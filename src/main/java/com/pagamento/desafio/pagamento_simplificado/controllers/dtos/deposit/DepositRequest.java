package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.deposit;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private Long userId;
    private BigDecimal amount;
}
