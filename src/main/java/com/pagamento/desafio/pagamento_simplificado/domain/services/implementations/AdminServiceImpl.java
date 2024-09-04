package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.admin.AdminOperationException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.AdminService;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import com.pagamento.desafio.pagamento_simplificado.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator<Admin> adminValidator;

    @Override
    public void registerAdmin(Admin admin) {
        adminValidator.validate(admin);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with id " + id + " not found."));
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        Admin existingAdmin = getAdminById(id);
        try {
            existingAdmin.setName(updatedAdmin.getName());
            existingAdmin.setEmail(updatedAdmin.getEmail());
            if (updatedAdmin.getPassword() != null) {
                existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
            }
            return adminRepository.save(existingAdmin);
        } catch (Exception e) {
            throw new AdminOperationException("Failed to update admin with id " + id);
        }
    }

    @Override
    public Admin partialUpdateAdmin(Long id, AdminUpdateRequest adminUpdateRequest) {
        Admin existingAdmin = getAdminById(id);
        try {
            if (adminUpdateRequest.getName() != null) {
                existingAdmin.setName(adminUpdateRequest.getName());
            }
            if (adminUpdateRequest.getEmail() != null) {
                existingAdmin.setEmail(adminUpdateRequest.getEmail());
            }
            if (adminUpdateRequest.getPassword() != null) {
                existingAdmin.setPassword(passwordEncoder.encode(adminUpdateRequest.getPassword()));
            }
            return adminRepository.save(existingAdmin);
        } catch (Exception e) {
            throw new AdminOperationException("Failed to partially update admin with id " + id);
        }
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = getAdminById(id);
        try {
            adminRepository.delete(admin);
        } catch (Exception e) {
            throw new AdminOperationException("Failed to delete admin with id " + id);
        }
    }
}
