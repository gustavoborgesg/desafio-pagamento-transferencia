package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.user;

import lombok.Data;

@Data
public class UserAccountDefaultResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
}
