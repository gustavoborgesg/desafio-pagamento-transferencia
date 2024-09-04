package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull(message = "Payer ID is required")
    @Min(value = 1, message = "Payer ID must be a positive number")
    private Long payer;

    @NotNull(message = "Payee ID is required")
    @Min(value = 1, message = "Payee ID must be a positive number")
    private Long payee;

    @NotNull(message = "Transfer value is required")
    @DecimalMin(value = "0.01", message = "Transfer value must be greater than zero")
    private BigDecimal value;
}
