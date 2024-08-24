package com.pagamento.desafio.pagamento_simplificado.domain.entities;

import com.pagamento.desafio.pagamento_simplificado.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Merchant extends Account {

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Override
    public String getIdentifier() {
        return cnpj;
    }
}
