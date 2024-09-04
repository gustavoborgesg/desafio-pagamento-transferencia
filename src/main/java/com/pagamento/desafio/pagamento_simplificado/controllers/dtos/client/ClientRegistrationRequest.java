package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientRegistrationRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "CPF is mandatory")
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 digits")
    private String cpf;

    @NotNull(message = "Initial balance is mandatory")
    @DecimalMin(value = "0.01", message = "Initial balance must be at least 0.01")
    private BigDecimal initialBalance;
}
