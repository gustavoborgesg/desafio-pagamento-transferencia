package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.auth.AuthenticationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.auth.AuthenticationResponse;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.auth.InvalidCredentialsException;
import com.pagamento.desafio.pagamento_simplificado.infra.security.CustomUserDetails;
import com.pagamento.desafio.pagamento_simplificado.infra.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Should successfully authenticate a user and return a JWT token")
    public void testLoginUser_Success() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("user@test.com");
        request.setPassword("password");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenUtil.generateToken(eq(customUserDetails))).thenReturn("mocked-jwt-token");

        // Act
        ResponseEntity<AuthenticationResponse> response = authController.loginUser(request);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getJwt()).isEqualTo("mocked-jwt-token");

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtil, times(1)).generateToken(eq(customUserDetails));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException for wrong password")
    public void testLoginUser_InvalidCredentials() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("user@test.com");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authController.loginUser(request));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtil, never()).generateToken(any(CustomUserDetails.class));
    }
}
