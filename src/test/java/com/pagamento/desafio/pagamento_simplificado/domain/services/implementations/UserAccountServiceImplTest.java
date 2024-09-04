package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Wallet;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.user.UserAccountNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount() {
            @Override
            public String getIdentifier() {
                return "12345678900";
            }
        };
        userAccount.setId(1L);
        Wallet wallet = new Wallet();
        wallet.credit(new BigDecimal("100.00"));
        userAccount.setWallet(wallet);
    }

    @Test
    @DisplayName("Deve depositar com sucesso na carteira do usuário")
    void depositToWallet_Success() {
        // Arrange
        when(userAccountRepository.findById(userAccount.getId())).thenReturn(Optional.of(userAccount));

        // Act
        userAccountService.depositToWallet(userAccount.getId(), new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), userAccount.getWallet().getBalance());
        verify(userAccountRepository).findById(userAccount.getId());
        verify(userAccountRepository).save(userAccount);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado")
    void depositToWallet_Failure_UserNotFound() {
        // Arrange
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserAccountNotFoundException.class, () -> userAccountService.depositToWallet(1L, new BigDecimal("50.00")));

        verify(userAccountRepository).findById(anyLong());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }
}
