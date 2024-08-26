package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client.ClientAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client.ClientNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.ClientService;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Client registerClient(Client client) {
        validateClientUniqueness(client);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found."));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Long id, Client updatedClient) {
        Client existingClient = getClientById(id);
        existingClient.setCpf(updatedClient.getCpf());
        existingClient.setName(updatedClient.getName());
        existingClient.setEmail(updatedClient.getEmail());

        if (updatedClient.getPassword() != null) {
            existingClient.setPassword(passwordEncoder.encode(updatedClient.getPassword()));
        }

        return clientRepository.save(existingClient);
    }

    @Override
    public Client partialUpdateClient(Long id, ClientUpdateRequest clientUpdateRequest) {
        Client existingClient = getClientById(id);

        if (clientUpdateRequest.getEmail() != null) {
            existingClient.setEmail(clientUpdateRequest.getEmail());
        }
        if (clientUpdateRequest.getName() != null) {
            existingClient.setName(clientUpdateRequest.getName());
        }
        if (clientUpdateRequest.getPassword() != null) {
            existingClient.setPassword(passwordEncoder.encode(clientUpdateRequest.getPassword()));
        }

        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }

    private void validateClientUniqueness(Client client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with email " + client.getEmail() + " already exists.");
        }
        if (clientRepository.findByCpf(client.getCpf()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with CPF " + client.getCpf() + " already exists.");
        }
    }
}
