package com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MerchantUpdateRequest {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
