package com.pagamento.desafio.pagamento_simplificado.domain.services;

import java.math.BigDecimal;

public interface UserAccountService {
    void depositToWallet(Long userId, BigDecimal amount);
}
