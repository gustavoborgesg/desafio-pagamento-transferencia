package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.AuthenticationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.AuthenticationResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.exception.auth.InvalidCredentialsException;
import com.pagamento.desafio.pagamento_simplificado.infra.security.CustomUserDetails;
import com.pagamento.desafio.pagamento_simplificado.infra.security.JwtTokenUtil;
import com.pagamento.desafio.pagamento_simplificado.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@RequestBody ClientRegistrationRequest clientRequest) {
        userService.registerClient(clientRequest);
        return ResponseEntity.ok("Client registered successfully");
    }

    @PostMapping("/register/merchant")
    public ResponseEntity<String> registerMerchant(@RequestBody MerchantRegistrationRequest merchantRequest) {
        userService.registerMerchant(merchantRequest);
        return ResponseEntity.ok("Merchant registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }
}
