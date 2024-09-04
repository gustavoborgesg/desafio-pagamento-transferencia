package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminOperationException;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
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
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator<Admin> adminValidator;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setName("John Doe");
        admin.setEmail("admin@example.com");
        admin.setCpf("12345678900");
        admin.setPassword("password");
    }

    @Test
    @DisplayName("Deve registrar admin com sucesso")
    void registerAdmin_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        // Act
        adminService.registerAdmin(admin);

        // Assert
        verify(adminValidator).validate(admin);
        verify(adminRepository).save(admin);
        assertEquals("encodedPassword", admin.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail já existir ao registrar admin")
    void registerAdmin_Failure_EmailAlreadyExists() {
        // Arrange
        doThrow(new AdminAlreadyExistsException("Admin with email already exists")).when(adminValidator).validate(any(Admin.class));

        // Act & Assert
        assertThrows(AdminAlreadyExistsException.class, () -> adminService.registerAdmin(admin));

        verify(adminValidator).validate(admin);
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CPF já existir ao registrar admin")
    void registerAdmin_Failure_CpfAlreadyExists() {
        // Arrange
        doThrow(new AdminAlreadyExistsException("Admin with CPF already exists")).when(adminValidator).validate(any(Admin.class));

        // Act & Assert
        assertThrows(AdminAlreadyExistsException.class, () -> adminService.registerAdmin(admin));

        verify(adminValidator).validate(admin);
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    @DisplayName("Deve obter admin pelo ID com sucesso")
    void getAdminById_Success() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        // Act
        Admin foundAdmin = adminService.getAdminById(1L);

        // Assert
        assertNotNull(foundAdmin);
        assertEquals(admin.getName(), foundAdmin.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando admin não for encontrado pelo ID")
    void getAdminById_Failure_NotFound() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.getAdminById(1L));
    }

    @Test
    @DisplayName("Deve listar todos os admins com sucesso")
    void getAllAdmins_Success() {
        // Arrange
        when(adminRepository.findAll()).thenReturn(List.of(admin));

        // Act
        List<Admin> admins = adminService.getAllAdmins();

        // Assert
        assertFalse(admins.isEmpty());
        assertEquals(1, admins.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver admins")
    void getAllAdmins_EmptyList() {
        // Arrange
        when(adminRepository.findAll()).thenReturn(List.of());

        // Act
        List<Admin> admins = adminService.getAllAdmins();

        // Assert
        assertNotNull(admins);
        assertTrue(admins.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar admin com sucesso")
    void updateAdmin_Success() {
        // Arrange
        Admin updatedAdmin = new Admin();
        updatedAdmin.setName("New Name");
        updatedAdmin.setEmail("newemail@example.com");
        updatedAdmin.setPassword("newPassword");

        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode(anyString())).thenReturn("newPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        // Act
        Admin result = adminService.updateAdmin(1L, updatedAdmin);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("newPassword", result.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar admin com falha")
    void updateAdmin_Failure() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(AdminOperationException.class, () -> adminService.updateAdmin(1L, admin));
    }

    @Test
    @DisplayName("Deve deletar admin com sucesso")
    void deleteAdmin_Success() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        // Act
        adminService.deleteAdmin(1L);

        // Assert
        verify(adminRepository).delete(admin);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar admin que não existe")
    void deleteAdmin_Failure_AdminNotFound() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdminNotFoundException.class, () -> adminService.deleteAdmin(1L));
        verify(adminRepository, never()).delete(any(Admin.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar admin com falha")
    void deleteAdmin_Failure() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        doThrow(new RuntimeException()).when(adminRepository).delete(any(Admin.class));

        // Act & Assert
        assertThrows(AdminOperationException.class, () -> adminService.deleteAdmin(1L));
    }
}
