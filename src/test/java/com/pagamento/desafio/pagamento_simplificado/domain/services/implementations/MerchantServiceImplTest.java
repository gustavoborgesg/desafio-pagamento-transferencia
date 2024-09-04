package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.merchant.MerchantNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
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
class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator<Merchant> merchantValidator;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Merchant 1");
        merchant.setEmail("merchant@example.com");
        merchant.setCnpj("12345678901234");
        merchant.setPassword("password");
    }

    @Test
    @DisplayName("Deve registrar merchant com sucesso")
    void registerMerchant_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        // Act
        merchantService.registerMerchant(merchant);

        // Assert
        verify(merchantValidator).validate(merchant);
        verify(merchantRepository).save(merchant);
        assertEquals("encodedPassword", merchant.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a validação do merchant falhar ao registrar merchant")
    void registerMerchant_Failure_ValidationError() {
        // Arrange
        doThrow(new RuntimeException("Validation error")).when(merchantValidator).validate(any(Merchant.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> merchantService.registerMerchant(merchant));
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    @Test
    @DisplayName("Deve obter merchant pelo ID com sucesso")
    void getMerchantById_Success() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));

        // Act
        Merchant foundMerchant = merchantService.getMerchantById(1L);

        // Assert
        assertNotNull(foundMerchant);
        assertEquals(merchant.getName(), foundMerchant.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando merchant não for encontrado pelo ID")
    void getMerchantById_Failure_NotFound() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MerchantNotFoundException.class, () -> merchantService.getMerchantById(1L));
    }

    @Test
    @DisplayName("Deve listar todos os merchants com sucesso")
    void getAllMerchants_Success() {
        // Arrange
        when(merchantRepository.findAll()).thenReturn(List.of(merchant));

        // Act
        List<Merchant> merchants = merchantService.getAllMerchants();

        // Assert
        assertFalse(merchants.isEmpty());
        assertEquals(1, merchants.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver merchants")
    void getAllMerchants_EmptyList() {
        // Arrange
        when(merchantRepository.findAll()).thenReturn(List.of());

        // Act
        List<Merchant> merchants = merchantService.getAllMerchants();

        // Assert
        assertNotNull(merchants);
        assertTrue(merchants.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar merchant com sucesso")
    void updateMerchant_Success() {
        // Arrange
        Merchant updatedMerchant = new Merchant();
        updatedMerchant.setName("New Merchant");
        updatedMerchant.setEmail("newemail@example.com");
        updatedMerchant.setCnpj("12345678901234");
        updatedMerchant.setPassword("newPassword");

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(passwordEncoder.encode(anyString())).thenReturn("newPassword");
        when(merchantRepository.save(any(Merchant.class))).thenReturn(updatedMerchant);

        // Act
        Merchant result = merchantService.updateMerchant(1L, updatedMerchant);

        // Assert
        assertEquals("New Merchant", result.getName());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("newPassword", result.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar merchant com falha")
    void updateMerchant_Failure() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any(Merchant.class))).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> merchantService.updateMerchant(1L, merchant));
    }

    @Test
    @DisplayName("Deve deletar merchant com sucesso")
    void deleteMerchant_Success() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));

        // Act
        merchantService.deleteMerchant(1L);

        // Assert
        verify(merchantRepository).delete(merchant);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar merchant que não existe")
    void deleteMerchant_Failure_MerchantNotFound() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MerchantNotFoundException.class, () -> merchantService.deleteMerchant(1L));
        verify(merchantRepository, never()).delete(any(Merchant.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar merchant com falha")
    void deleteMerchant_Failure() {
        // Arrange
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        doThrow(new RuntimeException()).when(merchantRepository).delete(any(Merchant.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> merchantService.deleteMerchant(1L));
    }
}
