package com.pagamento.desafio.pagamento_simplificado.infra.security;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new CustomUserDetails(userAccount);
    }
}
