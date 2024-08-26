package com.pagamento.desafio.pagamento_simplificado.external.dtos;

import lombok.Data;

@Data
public class AuthorizationResponse {
    private String status;
    private DataDTO data;

    @Data
    public static class DataDTO {
        private Boolean authorization;
    }
}
