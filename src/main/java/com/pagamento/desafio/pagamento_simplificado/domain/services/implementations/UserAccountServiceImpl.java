package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.user.UserAccountNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.UserAccountService;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserAccount depositToWallet(Long userId, BigDecimal amount) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserAccountNotFoundException("User with id " + userId + " not found."));
        user.getWallet().credit(amount);
        return userAccountRepository.save(user);
    }
}
