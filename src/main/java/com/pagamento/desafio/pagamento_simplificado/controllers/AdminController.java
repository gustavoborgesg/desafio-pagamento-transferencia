package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.admin.AdminUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.services.AdminService;
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
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest adminUpdateRequest) {
        Admin updatedAdmin = adminService.updateAdmin(id, mapToEntity(adminUpdateRequest));
        return ResponseEntity.ok(updatedAdmin);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Admin> partialUpdateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequest adminUpdateRequest) {
        Admin updatedAdmin = adminService.partialUpdateAdmin(id, adminUpdateRequest);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Admin deleted successfully");
    }

    private Admin mapToEntity(AdminRegistrationRequest adminRequest) {
        Admin admin = new Admin();
        admin.setUsername(adminRequest.getUsername());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());
        return admin;
    }

    private Admin mapToEntity(AdminUpdateRequest adminUpdateRequest) {
        Admin admin = new Admin();
        admin.setEmail(adminUpdateRequest.getEmail());
        admin.setUsername(adminUpdateRequest.getUsername());
        if (adminUpdateRequest.getPassword() != null) {
            admin.setPassword(adminUpdateRequest.getPassword());
        }
        return admin;
    }
}
