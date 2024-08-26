package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminDefaultResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegistrationRequest adminRequest) {
        Admin admin = mapToEntity(adminRequest);
        adminService.registerAdmin(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDefaultResponse> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(mapToResponse(admin));
    }

    @GetMapping
    public ResponseEntity<List<AdminDefaultResponse>> getAllAdmins() {
        List<AdminDefaultResponse> admins = adminService.getAllAdmins().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDefaultResponse> updateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest adminUpdateRequest) {
        Admin updatedAdmin = adminService.updateAdmin(id, mapToEntity(adminUpdateRequest));
        return ResponseEntity.ok(mapToResponse(updatedAdmin));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminDefaultResponse> partialUpdateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest adminUpdateRequest) {
        Admin updatedAdmin = adminService.partialUpdateAdmin(id, adminUpdateRequest);
        return ResponseEntity.ok(mapToResponse(updatedAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Admin deleted successfully");
    }

    private Admin mapToEntity(AdminRegistrationRequest adminRequest) {
        Admin admin = new Admin();
        admin.setCpf(adminRequest.getCpf());
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());
        return admin;
    }

    private Admin mapToEntity(AdminUpdateRequest adminUpdateRequest) {
        Admin admin = new Admin();
        admin.setName(adminUpdateRequest.getName());
        admin.setEmail(adminUpdateRequest.getEmail());
        if (adminUpdateRequest.getPassword() != null) {
            admin.setPassword(adminUpdateRequest.getPassword());
        }
        return admin;
    }

    private AdminDefaultResponse mapToResponse(Admin admin) {
        AdminDefaultResponse response = new AdminDefaultResponse();
        response.setId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        return response;
    }
}
