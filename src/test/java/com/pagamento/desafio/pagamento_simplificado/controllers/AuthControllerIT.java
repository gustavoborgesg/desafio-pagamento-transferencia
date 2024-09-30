package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.auth.AuthenticationRequest;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerIT extends BaseIntegrationTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminController adminController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        adminRepository.deleteAll();

        AdminRegistrationRequest adminRequest = new AdminRegistrationRequest();
        adminRequest.setCpf("12345678902");
        adminRequest.setName("Admin User");
        adminRequest.setEmail("admin@test.com");
        adminRequest.setPassword("admin123");
        adminController.registerAdmin(adminRequest);
    }

    @Test
    @DisplayName("Should successfully authenticate a user and return a JWT token")
    public void testLoginUser_Success() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("admin@test.com");
        request.setPassword("admin123");

        String jsonRequest = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    assertThat(jsonResponse).contains("jwt");
                });
    }

    @Test
    @DisplayName("Should fail to authenticate with invalid credentials")
    public void testLoginUser_Failure_InvalidCredentials() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("admin@test.com");
        request.setPassword("wrongpassword");

        String jsonRequest = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());  // Expect 401 Unauthorized
    }
}
