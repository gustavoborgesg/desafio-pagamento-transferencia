package com.pagamento.desafio.pagamento_simplificado.external.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String email;
    private String message;
}
