package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.deposit.DepositRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.repositories.ClientRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class UserAccountControllerIT extends BaseIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientController clientController;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MerchantController merchantController;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should successfully deposit to a client's wallet")
    public void testDepositToWallet_Success_Client() throws Exception {
        // Arrange
        clientRepository.deleteAll(); // Clean all clients

        ClientRegistrationRequest clientRequest = new ClientRegistrationRequest();
        clientRequest.setName("Client User");
        clientRequest.setEmail("client@test.com");
        clientRequest.setPassword("client123");
        clientRequest.setCpf("12345678901");
        clientRequest.setInitialBalance(new BigDecimal("100.00"));

        var clientResponse = clientController.registerClient(clientRequest);
        Long clientId = Objects.requireNonNull(clientResponse.getBody()).getId();

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setUserId(clientId);
        depositRequest.setAmount(new BigDecimal("50.00"));

        String jsonDepositRequest = objectMapper.writeValueAsString(depositRequest);

        // Act & Assert
        mockMvc.perform(post("/users/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDepositRequest))
                .andExpect(status().isOk());

        // Assert wallet balance updated
        Client updatedClient = (Client) userAccountRepository.findById(clientId).orElse(null);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getWallet().getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Should successfully deposit to a merchant's wallet")
    public void testDepositToWallet_Success_Merchant() throws Exception {
        // Arrange
        merchantRepository.deleteAll(); // Clean all merchants

        MerchantRegistrationRequest merchantRequest = new MerchantRegistrationRequest();
        merchantRequest.setName("Merchant User");
        merchantRequest.setEmail("merchant@test.com");
        merchantRequest.setPassword("merchant123");
        merchantRequest.setCnpj("12345678000199");
        merchantRequest.setInitialBalance(new BigDecimal("200.00"));

        var merchantResponse = merchantController.registerMerchant(merchantRequest);
        Long merchantId = Objects.requireNonNull(merchantResponse.getBody()).getId();

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setUserId(merchantId);
        depositRequest.setAmount(new BigDecimal("100.00"));

        String jsonDepositRequest = objectMapper.writeValueAsString(depositRequest);

        // Act & Assert
        mockMvc.perform(post("/users/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDepositRequest))
                .andExpect(status().isOk());

        // Assert wallet balance updated
        Merchant updatedMerchant = (Merchant) userAccountRepository.findById(merchantId).orElse(null);
        assertThat(updatedMerchant).isNotNull();
        assertThat(updatedMerchant.getWallet().getBalance()).isEqualTo(new BigDecimal("300.00"));
    }

    @Test
    @DisplayName("Should return 404 when user is not found for deposit")
    public void testDepositToWallet_Failure_UserNotFound() throws Exception {
        // Arrange
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setUserId(999L);  // Non-existing ID
        depositRequest.setAmount(new BigDecimal("50.00"));

        String jsonDepositRequest = objectMapper.writeValueAsString(depositRequest);

        // Act & Assert
        mockMvc.perform(post("/users/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDepositRequest))
                .andExpect(status().isNotFound());  // Expect 404 Not Found
    }

    @Test
    @DisplayName("Should return 400 for invalid deposit amount")
    public void testDepositToWallet_Failure_InvalidAmount() throws Exception {
        // Arrange
        clientRepository.deleteAll(); // Clean all clients

        ClientRegistrationRequest clientRequest = new ClientRegistrationRequest();
        clientRequest.setName("Client User");
        clientRequest.setEmail("client@test.com");
        clientRequest.setPassword("client123");
        clientRequest.setCpf("12345678901");
        clientRequest.setInitialBalance(new BigDecimal("100.00"));

        var clientResponse = clientController.registerClient(clientRequest);
        Long clientId = Objects.requireNonNull(clientResponse.getBody()).getId();

        // Prepare invalid deposit request
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setUserId(clientId);
        depositRequest.setAmount(new BigDecimal("0.00"));  // Invalid amount

        String jsonDepositRequest = objectMapper.writeValueAsString(depositRequest);

        // Act & Assert
        mockMvc.perform(post("/users/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDepositRequest))
                .andExpect(status().isBadRequest());  // Expect 400 Bad Request
    }
}
