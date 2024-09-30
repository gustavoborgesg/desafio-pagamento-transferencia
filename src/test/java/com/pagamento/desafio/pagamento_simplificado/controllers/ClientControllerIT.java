package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ClientControllerIT extends BaseIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully register a new client")
    public void testRegisterClient_Success() throws Exception {
        // Arrange
        ClientRegistrationRequest clientRequest = new ClientRegistrationRequest();
        clientRequest.setCpf("12345678902");
        clientRequest.setName("Client User");
        clientRequest.setEmail("client@test.com");
        clientRequest.setPassword("client123");
        clientRequest.setInitialBalance(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(clientRequest);

        // Act
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Client savedClient = clientRepository.findByEmail("client@test.com").orElse(null);
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getCpf()).isEqualTo("12345678902");
        assertThat(savedClient.getName()).isEqualTo("Client User");
        assertThat(savedClient.getEmail()).isEqualTo("client@test.com");
    }

    @Test
    @DisplayName("Should return client details by ID")
    public void testGetClientById_Success() throws Exception {
        // Arrange
        Client client = new Client();
        client.setCpf("12345678901");
        client.setName("Client User");
        client.setEmail("client@test.com");
        client.setPassword(passwordEncoder.encode("client123"));
        client.getWallet().setBalance(new BigDecimal("100.00"));
        client = clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(get("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 Not Found if client ID does not exist")
    public void testGetClientById_Failure_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/clients/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting 404 Not Found
    }

    @Test
    @DisplayName("Should update client details successfully")
    public void testUpdateClient_Success() throws Exception {
        // Arrange
        Client client = new Client();
        client.setCpf("12345678901");
        client.setName("Client User");
        client.setEmail("client@test.com");
        client.setPassword(passwordEncoder.encode("client123"));
        client.getWallet().setBalance(new BigDecimal("100.00"));
        client = clientRepository.save(client);

        ClientRegistrationRequest updateRequest = new ClientRegistrationRequest();
        updateRequest.setName("Updated Client");
        updateRequest.setEmail("updatedclient@test.com");
        updateRequest.setPassword("updatedpassword");
        updateRequest.setCpf("12345678901");
        updateRequest.setInitialBalance(new BigDecimal("200.00"));

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // Act
        mockMvc.perform(put("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Updated Client");
        assertThat(updatedClient.getEmail()).isEqualTo("updatedclient@test.com");
    }

    @Test
    @DisplayName("Should partially update client details successfully")
    public void testPartialUpdateClient_Success() throws Exception {
        // Arrange
        Client client = new Client();
        client.setCpf("12345678901");
        client.setName("Client User");
        client.setEmail("client@test.com");
        client.setPassword(passwordEncoder.encode("client123"));
        client.getWallet().setBalance(new BigDecimal("100.00"));
        client = clientRepository.save(client);

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Partially Updated Client");

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // Act
        mockMvc.perform(patch("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Partially Updated Client");
    }

    @Test
    @DisplayName("Should delete client successfully")
    public void testDeleteClient_Success() throws Exception {
        // Arrange
        Client client = new Client();
        client.setCpf("12345678901");
        client.setName("Client User");
        client.setEmail("client@test.com");
        client.setPassword(passwordEncoder.encode("client123"));
        client.getWallet().setBalance(new BigDecimal("100.00"));
        client = clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(delete("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Client deletedClient = clientRepository.findById(client.getId()).orElse(null);
        assertThat(deletedClient).isNull();
    }

    @Test
    @DisplayName("Should return 404 Not Found when trying to delete non-existent client")
    public void testDeleteClient_Failure_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/clients/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting 404 Not Found
    }
}
