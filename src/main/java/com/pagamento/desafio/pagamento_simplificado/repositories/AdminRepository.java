package com.pagamento.desafio.pagamento_simplificado.repositories;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByCpf(String cpf);
}
