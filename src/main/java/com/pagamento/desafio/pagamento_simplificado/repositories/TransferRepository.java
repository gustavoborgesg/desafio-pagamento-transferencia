package com.pagamento.desafio.pagamento_simplificado.repositories;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
