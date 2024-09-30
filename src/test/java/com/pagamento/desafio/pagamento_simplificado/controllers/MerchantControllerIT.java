package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
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
public class MerchantControllerIT extends BaseIntegrationTest {

    @Autowired
    private MerchantRepository merchantRepository;

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
        merchantRepository.deleteAll();  // Clear database before each test
    }

    @Test
    @DisplayName("Should successfully register a new merchant")
    public void testRegisterMerchant_Success() throws Exception {
        // Arrange
        MerchantRegistrationRequest merchantRequest = new MerchantRegistrationRequest();
        merchantRequest.setCnpj("12345678000199");
        merchantRequest.setName("Merchant User");
        merchantRequest.setEmail("merchant@test.com");
        merchantRequest.setPassword("merchant123");
        merchantRequest.setInitialBalance(new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(merchantRequest);

        // Act
        mockMvc.perform(post("/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Merchant savedMerchant = merchantRepository.findByEmail("merchant@test.com").orElse(null);
        assertThat(savedMerchant).isNotNull();
        assertThat(savedMerchant.getCnpj()).isEqualTo("12345678000199");
        assertThat(savedMerchant.getName()).isEqualTo("Merchant User");
        assertThat(savedMerchant.getEmail()).isEqualTo("merchant@test.com");
    }

    @Test
    @DisplayName("Should return merchant details by ID")
    public void testGetMerchantById_Success() throws Exception {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setCnpj("12345678000199");
        merchant.setName("Merchant User");
        merchant.setEmail("merchant@test.com");
        merchant.setPassword(passwordEncoder.encode("merchant123"));
        merchant.getWallet().setBalance(new BigDecimal("100.00"));
        merchant = merchantRepository.save(merchant);

        // Act & Assert
        mockMvc.perform(get("/merchants/{id}", merchant.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 Not Found if merchant ID does not exist")
    public void testGetMerchantById_Failure_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/merchants/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting 404 Not Found
    }

    @Test
    @DisplayName("Should update merchant details successfully")
    public void testUpdateMerchant_Success() throws Exception {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setCnpj("12345678000199");
        merchant.setName("Merchant User");
        merchant.setEmail("merchant@test.com");
        merchant.setPassword(passwordEncoder.encode("merchant123"));
        merchant.getWallet().setBalance(new BigDecimal("100.00"));
        merchant = merchantRepository.save(merchant);

        MerchantRegistrationRequest updateRequest = new MerchantRegistrationRequest();
        updateRequest.setCnpj("12345678000199");
        updateRequest.setName("Updated Merchant");
        updateRequest.setEmail("updatedmerchant@test.com");
        updateRequest.setPassword("updatedpassword");
        updateRequest.setInitialBalance(new BigDecimal("200.00"));

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // Act
        mockMvc.perform(put("/merchants/{id}", merchant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).orElse(null);
        assertThat(updatedMerchant).isNotNull();
        assertThat(updatedMerchant.getName()).isEqualTo("Updated Merchant");
        assertThat(updatedMerchant.getEmail()).isEqualTo("updatedmerchant@test.com");
    }

    @Test
    @DisplayName("Should partially update merchant details successfully")
    public void testPartialUpdateMerchant_Success() throws Exception {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setCnpj("12345678000199");
        merchant.setName("Merchant User");
        merchant.setEmail("merchant@test.com");
        merchant.setPassword(passwordEncoder.encode("merchant123"));
        merchant.getWallet().setBalance(new BigDecimal("100.00"));
        merchant = merchantRepository.save(merchant);

        MerchantUpdateRequest updateRequest = new MerchantUpdateRequest();
        updateRequest.setName("Partially Updated Merchant");

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // Act
        mockMvc.perform(patch("/merchants/{id}", merchant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).orElse(null);
        assertThat(updatedMerchant).isNotNull();
        assertThat(updatedMerchant.getName()).isEqualTo("Partially Updated Merchant");
    }

    @Test
    @DisplayName("Should delete merchant successfully")
    public void testDeleteMerchant_Success() throws Exception {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setCnpj("12345678000199");
        merchant.setName("Merchant User");
        merchant.setEmail("merchant@test.com");
        merchant.setPassword(passwordEncoder.encode("merchant123"));
        merchant.getWallet().setBalance(new BigDecimal("100.00"));
        merchant = merchantRepository.save(merchant);

        // Act & Assert
        mockMvc.perform(delete("/merchants/{id}", merchant.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Merchant deletedMerchant = merchantRepository.findById(merchant.getId()).orElse(null);
        assertThat(deletedMerchant).isNull();
    }

    @Test
    @DisplayName("Should return 404 Not Found when trying to delete non-existent merchant")
    public void testDeleteMerchant_Failure_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/merchants/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting 404 Not Found
    }
}
