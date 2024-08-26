package com.pagamento.desafio.pagamento_simplificado.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "system_users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ROLE_ADMIN, ROLE_CLIENT, or ROLE_MERCHANT

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet = new Wallet();

    public abstract String getIdentifier();
}
