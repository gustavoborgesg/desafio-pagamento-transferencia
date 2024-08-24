package com.pagamento.desafio.pagamento_simplificado.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Admin extends SystemUser {

    @Column(nullable = false, unique = true)
    private String cpf;

    public Admin() {
        super.setRole("ROLE_ADMIN"); // Automatically set the role
    }

    @Override
    public String getIdentifier() {
        return cpf;
    }
}
