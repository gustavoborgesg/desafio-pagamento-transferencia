package com.pagamento.desafio.pagamento_simplificado.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Merchant extends UserAccount {

    @Column(nullable = false, unique = true)
    private String cnpj;

    public Merchant() {
        super.setRole("ROLE_MERCHANT"); // Automatically set the role
    }

    @Override
    public String getIdentifier() {
        return cnpj;
    }
}
