package com.pagamento.desafio.pagamento_simplificado.infra.security;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.exceptions.client.ClientNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
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

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElse(null);

        if (admin != null) {
            return new CustomUserDetails(admin);
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new ClientNotFoundException("Client with email " + username + " not found."));

        if (client != null) {
            return new CustomUserDetails(client);
        }

        Merchant merchant = merchantRepository.findByEmail(username)
                .orElseThrow(() -> new ClientNotFoundException("Merchant with email " + username + " not found."));

        return new CustomUserDetails(merchant);
    }
}
