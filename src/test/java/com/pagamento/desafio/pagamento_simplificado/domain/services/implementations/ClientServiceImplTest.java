package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.client.ClientNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator<Client> clientValidator;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Jane Doe");
        client.setEmail("client@example.com");
        client.setCpf("12345678901");
        client.setPassword("password");
    }

    @Test
    @DisplayName("Should register client successfully")
    void registerClient_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        clientService.registerClient(client);

        // Assert
        verify(clientValidator).validate(client);
        verify(clientRepository).save(client);
        assertEquals("encodedPassword", client.getPassword());
    }

    @Test
    @DisplayName("Should throw exception when client validation fails during registration")
    void registerClient_Failure_ValidationError() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(clientValidator).validate(any(Client.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clientService.registerClient(client));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Should get client by ID successfully")
    void getClientById_Success() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        // Act
        Client foundClient = clientService.getClientById(1L);

        // Assert
        assertNotNull(foundClient);
        assertEquals(client.getName(), foundClient.getName());
    }

    @Test
    @DisplayName("Should throw exception when client is not found by ID")
    void getClientById_Failure_NotFound() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    @DisplayName("Should return all clients successfully")
    void getAllClients_Success() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(List.of(client));

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertFalse(clients.isEmpty());
        assertEquals(1, clients.size());
    }

    @Test
    @DisplayName("Should return an empty list when no clients exist")
    void getAllClients_EmptyList() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(List.of());

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertTrue(clients.isEmpty());
    }

    @Test
    @DisplayName("Should update client successfully")
    void updateClient_Success() {
        // Arrange
        Client updatedClient = new Client();
        updatedClient.setName("New Name");
        updatedClient.setEmail("newemail@example.com");
        updatedClient.setCpf("12345678901");
        updatedClient.setPassword("newPassword");

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(anyString())).thenReturn("newPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        // Act
        Client result = clientService.updateClient(1L, updatedClient);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("newPassword", result.getPassword());
    }

    @Test
    @DisplayName("Should throw exception when client update fails")
    void updateClient_Failure() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clientService.updateClient(1L, client));
    }

    @Test
    @DisplayName("Should delete client successfully")
    void deleteClient_Success() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        // Act
        clientService.deleteClient(1L);

        // Assert
        verify(clientRepository).delete(client);
    }

    @Test
    @DisplayName("Should throw exception when client to be deleted is not found")
    void deleteClient_Failure_ClientNotFound() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(1L));
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    @DisplayName("Should throw exception when client deletion fails")
    void deleteClient_Failure() {
        // Arrange
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        doThrow(new RuntimeException()).when(clientRepository).delete(any(Client.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clientService.deleteClient(1L));
    }
}
