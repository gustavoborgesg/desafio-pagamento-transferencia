package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.UserAccount;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Wallet;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.InsufficientBalanceException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotAllowedException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.transfer.TransferNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.external.dtos.NotificationRequest;
import com.pagamento.desafio.pagamento_simplificado.external.services.TransferRestClient;
import com.pagamento.desafio.pagamento_simplificado.repositories.TransferRepository;
import com.pagamento.desafio.pagamento_simplificado.repositories.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private TransferRestClient transferRestClient;

    @InjectMocks
    private TransferServiceImpl transferService;

    private TransferRequest transferRequest;
    private UserAccount payer;
    private UserAccount payee;
    private Transfer transfer;

    @BeforeEach
    void setUp() {
        payer = new UserAccount() {
            @Override
            public String getIdentifier() {
                return "PAYER_IDENTIFIER";
            }
        };
        payer.setId(1L);
        payer.setRole("ROLE_CLIENT");
        payer.setWallet(new Wallet());
        payer.getWallet().credit(new BigDecimal("100.00"));

        payee = new UserAccount() {
            @Override
            public String getIdentifier() {
                return "PAYEE_IDENTIFIER";
            }
        };
        payee.setId(2L);
        payee.setRole("ROLE_MERCHANT");
        payee.setWallet(new Wallet());
        payee.getWallet().credit(new BigDecimal("50.00"));

        transferRequest = new TransferRequest();
        transferRequest.setPayer(payer.getId());
        transferRequest.setPayee(payee.getId());
        transferRequest.setValue(new BigDecimal("20.00"));

        transfer = new Transfer();
        transfer.setPayer(payer);
        transfer.setPayee(payee);
        transfer.setAmount(new BigDecimal("20.00"));
        transfer.setTimestamp(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve executar transferência com sucesso")
    void executeTransfer_Success() {
        // Arrange
        when(userAccountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
        when(userAccountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);

        doNothing().when(transferRestClient).authorizeTransfer();
        doNothing().when(transferRestClient).notifyPayee(any(NotificationRequest.class));

        // Act
        Transfer result = transferService.executeTransfer(transferRequest);

        // Assert
        verify(userAccountRepository, times(2)).findById(anyLong());
        verify(transferRepository).save(any(Transfer.class));
        verify(transferRestClient).authorizeTransfer();
        verify(transferRestClient).notifyPayee(any(NotificationRequest.class));

        assertEquals(transfer.getAmount(), result.getAmount());
        assertEquals(transfer.getPayer(), result.getPayer());
        assertEquals(transfer.getPayee(), result.getPayee());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador não for encontrado")
    void executeTransfer_Failure_PayerNotFound() {
        // Arrange
        when(userAccountRepository.findById(payer.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransferNotFoundException.class, () -> transferService.executeTransfer(transferRequest));
        verify(userAccountRepository).findById(payer.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o recebedor não for encontrado")
    void executeTransfer_Failure_PayeeNotFound() {
        // Arrange
        when(userAccountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
        when(userAccountRepository.findById(payee.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransferNotFoundException.class, () -> transferService.executeTransfer(transferRequest));
        verify(userAccountRepository).findById(payee.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador for um Merchant")
    void executeTransfer_Failure_PayerIsMerchant() {
        // Arrange
        payer.setRole("ROLE_MERCHANT");
        when(userAccountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
        when(userAccountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));

        // Act & Assert
        assertThrows(TransferNotAllowedException.class, () -> transferService.executeTransfer(transferRequest));
        verify(userAccountRepository).findById(payer.getId());
        verify(userAccountRepository).findById(payee.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo insuficiente")
    void executeTransfer_Failure_InsufficientBalance() {
        // Arrange
        payer.getWallet().debit(new BigDecimal("90.00"));
        when(userAccountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
        when(userAccountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transferService.executeTransfer(transferRequest));
    }

    @Test
    @DisplayName("Deve obter transferência por ID com sucesso")
    void getTransferById_Success() {
        // Arrange
        when(transferRepository.findById(anyLong())).thenReturn(Optional.of(transfer));

        // Act
        Transfer result = transferService.getTransferById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(transfer.getAmount(), result.getAmount());
        verify(transferRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a transferência não for encontrada por ID")
    void getTransferById_Failure_TransferNotFound() {
        // Arrange
        when(transferRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransferNotFoundException.class, () -> transferService.getTransferById(1L));
        verify(transferRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve listar todas as transferências com sucesso")
    void getAllTransfers_Success() {
        // Arrange
        when(transferRepository.findAll()).thenReturn(List.of(transfer));

        // Act
        List<Transfer> transfers = transferService.getAllTransfers();

        // Assert
        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
        assertEquals(1, transfers.size());
        verify(transferRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver transferências")
    void getAllTransfers_EmptyList() {
        // Arrange
        when(transferRepository.findAll()).thenReturn(List.of());

        // Act
        List<Transfer> transfers = transferService.getAllTransfers();

        // Assert
        assertNotNull(transfers);
        assertTrue(transfers.isEmpty());
        verify(transferRepository).findAll();
    }
}
