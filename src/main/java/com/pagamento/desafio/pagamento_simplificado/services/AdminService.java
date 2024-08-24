package com.pagamento.desafio.pagamento_simplificado.services;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;

import java.util.List;

public interface AdminService {
    void registerAdmin(Admin admin);

    Admin getAdminById(Long id);

    List<Admin> getAllAdmins();

    Admin updateAdmin(Long id, Admin admin);

    Admin partialUpdateAdmin(Long id, AdminUpdateRequest adminUpdateRequest);

    void deleteAdmin(Long id);
}
