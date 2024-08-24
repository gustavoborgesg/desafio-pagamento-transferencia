package com.pagamento.desafio.pagamento_simplificado.domain.entities;

import com.pagamento.desafio.pagamento_simplificado.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Client extends Account {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Override
    public String getIdentifier() {
        return cpf;
    }
}
