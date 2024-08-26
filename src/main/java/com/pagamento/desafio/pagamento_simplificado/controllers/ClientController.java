package com.pagamento.desafio.pagamento_simplificado.controllers;

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

@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> registerClient(@RequestBody ClientRegistrationRequest clientRequest) {
        Client client = mapToEntity(clientRequest);
        Client savedClient = clientService.registerClient(client);
        return ResponseEntity.ok(savedClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientRegistrationRequest clientRequest) {
        Client updatedClient = clientService.updateClient(id, mapToEntity(clientRequest));
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Client> partialUpdateClient(@PathVariable Long id, @RequestBody ClientUpdateRequest clientUpdateRequest) {
        Client updatedClient = clientService.partialUpdateClient(id, clientUpdateRequest);
        return ResponseEntity.ok(updatedClient);
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
}
