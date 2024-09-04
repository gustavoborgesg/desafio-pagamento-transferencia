package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateRequest {
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
}
