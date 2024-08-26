package com.pagamento.desafio.pagamento_simplificado.domain.services;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;

import java.util.List;

public interface ClientService {
    Client registerClient(Client client);

    Client getClientById(Long id);

    List<Client> getAllClients();

    Client updateClient(Long id, Client client);

    Client partialUpdateClient(Long id, ClientUpdateRequest clientUpdateRequest);

    void deleteClient(Long id);
}
