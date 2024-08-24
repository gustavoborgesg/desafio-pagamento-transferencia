package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import com.pagamento.desafio.pagamento_simplificado.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final ClientRepository clientRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerClient(ClientRegistrationRequest clientRequest) {
        Client client = new Client();
        client.setCpf(clientRequest.getCpf());
        client.setEmail(clientRequest.getEmail());
        client.setName(clientRequest.getName());
        client.setPassword(passwordEncoder.encode(clientRequest.getPassword()));
        clientRepository.save(client);
    }

    @Override
    public void registerMerchant(MerchantRegistrationRequest merchantRequest) {
        Merchant merchant = new Merchant();
        merchant.setCnpj(merchantRequest.getCnpj());
        merchant.setEmail(merchantRequest.getEmail());
        merchant.setName(merchantRequest.getName());
        merchant.setPassword(passwordEncoder.encode(merchantRequest.getPassword()));
        merchantRepository.save(merchant);
    }
}
