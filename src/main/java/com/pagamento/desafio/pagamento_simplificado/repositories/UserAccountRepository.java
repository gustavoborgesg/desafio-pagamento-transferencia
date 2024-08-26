package com.pagamento.desafio.pagamento_simplificado.repositories;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
