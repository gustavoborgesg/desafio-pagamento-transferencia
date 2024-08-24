package com.pagamento.desafio.pagamento_simplificado.infra.security;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private ClientRepository clientRepository;
    private MerchantRepository merchantRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username)
                .orElse(null);

        if (client != null) {
            return new CustomUserDetails(client);
        }

        Merchant merchant = merchantRepository.findByEmail(username)
                .orElse(null);

        if (merchant != null) {
            return new CustomUserDetails(merchant);
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
