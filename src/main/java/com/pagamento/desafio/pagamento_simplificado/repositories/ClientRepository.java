package com.pagamento.desafio.pagamento_simplificado.repositories;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByCpf(String cpf);
}
