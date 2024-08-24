package com.pagamento.desafio.pagamento_simplificado.infra.auth;

import com.pagamento.desafio.pagamento_simplificado.infra.security.CustomUserDetails;
import com.pagamento.desafio.pagamento_simplificado.infra.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // Load the user details using the CustomUserDetailsService
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);

        // Check if the passwords match
        if (!passwordEncoder.matches(rawPassword, customUserDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Retrieve the user's authorities
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Return an authenticated token with the user's details and authorities
        return new UsernamePasswordAuthenticationToken(customUserDetails, rawPassword, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
