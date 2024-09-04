package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.deposit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    @NotNull(message = "User ID is mandatory")
    private Long userId;

    @NotNull(message = "Deposit value is mandatory")
    @DecimalMin(value = "0.01", message = "Deposit value must be at least 0.01")
    private BigDecimal amount;
}
