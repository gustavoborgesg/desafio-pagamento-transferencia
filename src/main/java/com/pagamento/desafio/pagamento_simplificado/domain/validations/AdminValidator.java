package com.pagamento.desafio.pagamento_simplificado.domain.validations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminValidator implements Validator<Admin> {

    private final AdminRepository adminRepository;

    @Override
    public void validate(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new AdminAlreadyExistsException("Admin with email " + admin.getEmail() + " already exists.");
        }

        if (adminRepository.findByCpf(admin.getCpf()).isPresent()) {
            throw new AdminAlreadyExistsException("Admin with CPF " + admin.getCpf() + " already exists.");
        }
    }
}
