package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRegistrationRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CPF is required")
    @Size(min = 11, max = 11, message = "CPF must be 11 characters long")
    private String cpf;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
}
