package com.pagamento.desafio.pagamento_simplificado.domain.validations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client.ClientAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientValidator implements Validator<Client> {

    private final ClientRepository clientRepository;

    @Override
    public void validate(Client client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with email " + client.getEmail() + " already exists.");
        }
        if (clientRepository.findByCpf(client.getCpf()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with CPF " + client.getCpf() + " already exists.");
        }
    }
}
