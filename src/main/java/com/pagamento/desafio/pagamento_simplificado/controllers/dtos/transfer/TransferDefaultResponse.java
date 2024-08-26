package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferDefaultResponse {
    private Long id;
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;
}
