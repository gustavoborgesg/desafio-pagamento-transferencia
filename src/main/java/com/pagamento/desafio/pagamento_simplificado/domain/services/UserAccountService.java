package com.pagamento.desafio.pagamento_simplificado.domain.services;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;

import java.math.BigDecimal;

public interface UserAccountService {
    UserAccount depositToWallet(Long userId, BigDecimal amount);
}
