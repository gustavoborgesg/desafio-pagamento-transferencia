package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.auth.AuthenticationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.auth.AuthenticationResponse;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.auth.InvalidCredentialsException;
import com.pagamento.desafio.pagamento_simplificado.infra.security.CustomUserDetails;
import com.pagamento.desafio.pagamento_simplificado.infra.security.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }
}
