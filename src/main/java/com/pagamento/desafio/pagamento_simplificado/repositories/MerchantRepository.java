package com.pagamento.desafio.pagamento_simplificado.repositories;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByEmail(String email);

}
