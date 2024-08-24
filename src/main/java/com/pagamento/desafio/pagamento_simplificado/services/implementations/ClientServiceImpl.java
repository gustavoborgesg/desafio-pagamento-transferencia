package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.exceptions.client.ClientAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.client.ClientNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.client.ClientOperationException;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import com.pagamento.desafio.pagamento_simplificado.services.ClientService;
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
    public void registerClient(Client client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with email " + client.getEmail() + " already exists.");
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
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
        try {
            Client existingClient = getClientById(id);
            existingClient.setEmail(updatedClient.getEmail());
            existingClient.setName(updatedClient.getName());
            if (updatedClient.getPassword() != null) {
                existingClient.setPassword(passwordEncoder.encode(updatedClient.getPassword()));
            }
            return clientRepository.save(existingClient);
        } catch (Exception e) {
            throw new ClientOperationException("Failed to update client with id " + id);
        }
    }

    @Override
    public Client partialUpdateClient(Long id, ClientUpdateRequest clientUpdateRequest) {
        try {
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
        } catch (Exception e) {
            throw new ClientOperationException("Failed to partially update client with id " + id);
        }
    }

    @Override
    public void deleteClient(Long id) {
        Client client = getClientById(id);
        try {
            clientRepository.delete(client);
        } catch (Exception e) {
            throw new ClientOperationException("Failed to delete client with id " + id);
        }
    }
}
