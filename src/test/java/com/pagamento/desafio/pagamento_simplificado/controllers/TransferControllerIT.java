package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.client.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferDefaultResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Client;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.external.exceptions.authorization.AuthorizationServiceException;
import com.pagamento.desafio.pagamento_simplificado.external.services.TransferRestClient;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class TransferControllerIT extends BaseIntegrationTest {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferRestClient transferRestClient;

    private MockMvc mockMvc;
    private static Long payerId;
    private static Long payeeId;

    @BeforeAll
    public static void setUpAccounts(@Autowired ClientController clientController, @Autowired MerchantController merchantController) {
        // Register Client (Payer)
        ClientRegistrationRequest clientRequest = new ClientRegistrationRequest();
        clientRequest.setName("Payer User");
        clientRequest.setEmail("payer@test.com");
        clientRequest.setPassword("payer123");
        clientRequest.setCpf("12345678901");
        clientRequest.setInitialBalance(new BigDecimal("500.00"));
        var payerResponse = clientController.registerClient(clientRequest);
        payerId = Objects.requireNonNull(payerResponse.getBody()).getId();

        // Register Merchant (Payee)
        MerchantRegistrationRequest merchantRequest = new MerchantRegistrationRequest();
        merchantRequest.setName("Payee Merchant");
        merchantRequest.setEmail("payee@test.com");
        merchantRequest.setPassword("payee123");
        merchantRequest.setCnpj("12345678000199");
        merchantRequest.setInitialBalance(new BigDecimal("200.00"));
        var payeeResponse = merchantController.registerMerchant(merchantRequest);
        payeeId = Objects.requireNonNull(payeeResponse.getBody()).getId();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        transferRepository.deleteAll();
    }

    @Test
    @DisplayName("Should execute transfer successfully")
    public void testExecuteTransfer_Success() throws Exception {
        // Arrange
        Mockito.doNothing().when(transferRestClient).authorizeTransfer();
        Mockito.doNothing().when(transferRestClient).notifyPayee(Mockito.any());

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayer(payerId);
        transferRequest.setPayee(payeeId);
        transferRequest.setValue(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        // Act
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Client updatedPayer = (Client) userAccountRepository.findById(payerId).orElse(null);
        Merchant updatedPayee = (Merchant) userAccountRepository.findById(payeeId).orElse(null);

        assertThat(updatedPayer).isNotNull();
        assertThat(updatedPayer.getWallet().getBalance()).isEqualTo(new BigDecimal("400.00"));
        assertThat(updatedPayee).isNotNull();
        assertThat(updatedPayee.getWallet().getBalance()).isEqualTo(new BigDecimal("300.00"));
    }

    @Test
    @DisplayName("Should fail due to insufficient balance")
    public void testExecuteTransfer_Failure_InsufficientBalance() throws Exception {
        // Arrange
        Client payer = (Client) userAccountRepository.findById(payerId).orElse(null);
        assert payer != null;
        payer.getWallet().setBalance(new BigDecimal("50.00"));
        userAccountRepository.save(payer);

        Mockito.doNothing().when(transferRestClient).authorizeTransfer();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayer(payerId);
        transferRequest.setPayee(payeeId);
        transferRequest.setValue(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        // Act & Assert
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail due to authorization denial")
    public void testExecuteTransfer_Failure_AuthorizationDenied() throws Exception {
        // Arrange
        Mockito.doThrow(new AuthorizationServiceException("Failed to receive a valid response from the authorization service", new Exception())).when(transferRestClient).authorizeTransfer();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayer(payerId);
        transferRequest.setPayee(payeeId);
        transferRequest.setValue(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        // Act & Assert
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail if Merchant attempts to transfer")
    public void testExecuteTransfer_Failure_MerchantAsPayer() throws Exception {
        // Arrange
        Mockito.doNothing().when(transferRestClient).authorizeTransfer();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayer(payeeId);
        transferRequest.setPayee(payerId);
        transferRequest.setValue(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        // Act & Assert
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should retrieve transfer by ID")
    public void testGetTransferById_Success() throws Exception {
        // Arrange
        Mockito.doNothing().when(transferRestClient).authorizeTransfer();
        Mockito.doNothing().when(transferRestClient).notifyPayee(Mockito.any());

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayer(payerId);
        transferRequest.setPayee(payeeId);
        transferRequest.setValue(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        // Execute transfer and capture response
        String transferResponse = mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract ID from created transfer
        TransferDefaultResponse createdTransfer = objectMapper.readValue(transferResponse, TransferDefaultResponse.class);
        Long createdTransferId = createdTransfer.getId();

        // Act & Assert: Retrieve transfer by ID
        mockMvc.perform(get("/transfers/{id}", createdTransferId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
