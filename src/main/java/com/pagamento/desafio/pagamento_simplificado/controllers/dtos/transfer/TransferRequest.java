package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private Long payerId;
    private Long payeeId;
    private BigDecimal value;
}
