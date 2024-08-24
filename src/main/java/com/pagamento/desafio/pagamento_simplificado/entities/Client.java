package com.pagamento.desafio.pagamento_simplificado.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Client extends SystemUser {

    @Column(nullable = false, unique = true)
    private String cpf;

    public Client() {
        super.setRole("ROLE_CLIENT");
    }

    @Override
    public String getIdentifier() {
        return cpf;
    }
}
