package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientDefaultResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDefaultResponse> registerClient(@RequestBody ClientRegistrationRequest clientRequest) {
        Client client = mapToEntity(clientRequest);
        Client savedClient = clientService.registerClient(client);
        return ResponseEntity.ok(mapToResponse(savedClient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDefaultResponse> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(mapToResponse(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientDefaultResponse>> getAllClients() {
        List<ClientDefaultResponse> clients = clientService.getAllClients().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDefaultResponse> updateClient(@PathVariable Long id, @RequestBody ClientRegistrationRequest clientRequest) {
        Client updatedClient = clientService.updateClient(id, mapToEntity(clientRequest));
        return ResponseEntity.ok(mapToResponse(updatedClient));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientDefaultResponse> partialUpdateClient(@PathVariable Long id, @RequestBody ClientUpdateRequest clientUpdateRequest) {
        Client updatedClient = clientService.partialUpdateClient(id, clientUpdateRequest);
        return ResponseEntity.ok(mapToResponse(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

    private Client mapToEntity(ClientRegistrationRequest clientRequest) {
        Client client = new Client();
        client.setCpf(clientRequest.getCpf());
        client.setName(clientRequest.getName());
        client.setEmail(clientRequest.getEmail());
        client.setPassword(clientRequest.getPassword());
        client.getWallet().setBalance(clientRequest.getInitialBalance());
        return client;
    }

    private ClientDefaultResponse mapToResponse(Client client) {
        ClientDefaultResponse response = new ClientDefaultResponse();
        response.setId(client.getId());
        response.setCpf(client.getCpf());
        response.setName(client.getName());
        response.setEmail(client.getEmail());
        response.setBalance(client.getWallet().getBalance());
        return response;
    }
}
