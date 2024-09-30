package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamento.desafio.pagamento_simplificado.BaseIntegrationTest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerIT extends BaseIntegrationTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        adminRepository.deleteAll();
    }

    // Testes de Registro
    @Test
    public void testRegisterAdmin_Success() throws Exception {
        // ARRANGE
        AdminRegistrationRequest adminRequest = new AdminRegistrationRequest();
        adminRequest.setCpf("12345678902");  // CPF único para este teste
        adminRequest.setName("Admin User");
        adminRequest.setEmail("admin2@test.com");  // E-mail único
        adminRequest.setPassword("admin123");

        String jsonRequest = objectMapper.writeValueAsString(adminRequest);

        // ACT
        mockMvc.perform(post("/admins/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // ASSERT
        Admin savedAdmin = adminRepository.findByEmail("admin2@test.com").orElse(null);
        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getCpf()).isEqualTo("12345678902");
        assertThat(savedAdmin.getName()).isEqualTo("Admin User");
        assertThat(savedAdmin.getEmail()).isEqualTo("admin2@test.com");
    }

    @Test
    public void testRegisterAdmin_Failure_DuplicateEmail() throws Exception {
        // ARRANGE
        Admin existingAdmin = new Admin();
        existingAdmin.setCpf("12345678901");
        existingAdmin.setName("Admin User");
        existingAdmin.setEmail("admin@test.com");
        existingAdmin.setPassword("admin123");
        adminRepository.save(existingAdmin);

        AdminRegistrationRequest adminRequest = new AdminRegistrationRequest();
        adminRequest.setCpf("12345678902");  // CPF diferente, mas e-mail duplicado
        adminRequest.setName("Another Admin");
        adminRequest.setEmail("admin@test.com");  // E-mail duplicado
        adminRequest.setPassword("admin123");

        String jsonRequest = objectMapper.writeValueAsString(adminRequest);

        // ACT
        mockMvc.perform(post("/admins/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());  // Espera-se um conflito (409)

        // ASSERT
        Admin duplicateAdmin = adminRepository.findByCpf("12345678902").orElse(null);
        assertThat(duplicateAdmin).isNull();  // O admin com CPF novo não deve ser registrado
    }

    @Test
    public void testRegisterAdmin_Failure_DuplicateCpf() throws Exception {
        // ARRANGE
        Admin existingAdmin = new Admin();
        existingAdmin.setCpf("12345678901");
        existingAdmin.setName("Admin User");
        existingAdmin.setEmail("admin@test.com");
        existingAdmin.setPassword("admin123");
        adminRepository.save(existingAdmin);

        AdminRegistrationRequest adminRequest = new AdminRegistrationRequest();
        adminRequest.setCpf("12345678901");  // CPF duplicado
        adminRequest.setName("Another Admin");
        adminRequest.setEmail("admin2@test.com");  // E-mail diferente
        adminRequest.setPassword("admin123");

        String jsonRequest = objectMapper.writeValueAsString(adminRequest);

        // ACT
        mockMvc.perform(post("/admins/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());  // Espera-se um conflito (409)

        // ASSERT
        Admin duplicateAdmin = adminRepository.findByEmail("admin2@test.com").orElse(null);
        assertThat(duplicateAdmin).isNull();  // O admin com o e-mail novo não deve ser registrado
    }

    // Testes de Consulta (GET)
    @Test
    public void testGetAdminById_Success() throws Exception {
        // ARRANGE
        Admin admin = new Admin();
        admin.setCpf("12345678901");
        admin.setName("Admin User");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin123");
        admin = adminRepository.save(admin);

        // ACT & ASSERT
        mockMvc.perform(get("/admins/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAdminById_Failure_NotFound() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/admins/{id}", 999L)  // ID inexistente
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Espera-se um 404 Not Found
    }

    // Testes de Atualização (PUT)
    @Test
    public void testUpdateAdmin_Success() throws Exception {
        // ARRANGE
        Admin admin = new Admin();
        admin.setCpf("12345678901");
        admin.setName("Admin User");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin123");
        admin = adminRepository.save(admin);

        AdminUpdateRequest updateRequest = new AdminUpdateRequest();
        updateRequest.setName("Updated Admin");
        updateRequest.setEmail("updated@test.com");

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // ACT
        mockMvc.perform(put("/admins/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        // ASSERT
        Admin updatedAdmin = adminRepository.findById(admin.getId()).orElse(null);
        assertThat(updatedAdmin).isNotNull();
        assertThat(updatedAdmin.getName()).isEqualTo("Updated Admin");
        assertThat(updatedAdmin.getEmail()).isEqualTo("updated@test.com");
    }

    @Test
    public void testUpdateAdmin_Failure_DuplicateEmail() throws Exception {
        // ARRANGE
        Admin admin1 = new Admin();
        admin1.setCpf("12345678901");
        admin1.setName("Admin One");
        admin1.setEmail("admin1@test.com");
        admin1.setPassword("admin123");
        adminRepository.save(admin1);

        Admin admin2 = new Admin();
        admin2.setCpf("12345678902");
        admin2.setName("Admin Two");
        admin2.setEmail("admin2@test.com");
        admin2.setPassword("admin123");
        admin2 = adminRepository.save(admin2);

        AdminUpdateRequest updateRequest = new AdminUpdateRequest();
        updateRequest.setEmail("admin1@test.com");  // Tentar atualizar para e-mail duplicado

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        // ACT
        mockMvc.perform(put("/admins/{id}", admin2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());  // Espera-se um conflito (409)
    }

    // Testes de Deleção (DELETE)
    @Test
    public void testDeleteAdmin_Success() throws Exception {
        // ARRANGE
        Admin admin = new Admin();
        admin.setCpf("12345678901");
        admin.setName("Admin User");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin123");
        admin = adminRepository.save(admin);

        // ACT & ASSERT
        mockMvc.perform(delete("/admins/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Admin deletedAdmin = adminRepository.findById(admin.getId()).orElse(null);
        assertThat(deletedAdmin).isNull();
    }

    @Test
    public void testDeleteAdmin_Failure_NotFound() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(delete("/admins/{id}", 999L)  // ID inexistente
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Espera-se um 404 Not Found
    }
}
