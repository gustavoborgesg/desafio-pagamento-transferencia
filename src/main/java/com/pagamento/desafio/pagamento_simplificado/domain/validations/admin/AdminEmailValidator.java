package com.pagamento.desafio.pagamento_simplificado.domain.validations.admin;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminEmailValidator {

    private final AdminRepository adminRepository;

    public void validate(String email) {
        if (adminRepository.findByEmail(email).isPresent()) {
            throw new AdminAlreadyExistsException("Admin with email " + email + " already exists.");
        }
    }
}
