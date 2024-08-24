package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.services.MerchantService;
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
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/register")
    public ResponseEntity<String> registerMerchant(@RequestBody MerchantRegistrationRequest merchantRequest) {
        Merchant merchant = mapToEntity(merchantRequest);
        merchantService.registerMerchant(merchant);
        return ResponseEntity.ok("Merchant registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchant> getMerchantById(@PathVariable Long id) {
        Merchant merchant = merchantService.getMerchantById(id);
        return ResponseEntity.ok(merchant);
    }

    @GetMapping
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        List<Merchant> merchants = merchantService.getAllMerchants();
        return ResponseEntity.ok(merchants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Merchant> updateMerchant(@PathVariable Long id, @RequestBody MerchantUpdateRequest merchantUpdateRequest) {
        Merchant updatedMerchant = merchantService.updateMerchant(id, mapToEntity(merchantUpdateRequest));
        return ResponseEntity.ok(updatedMerchant);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Merchant> partialUpdateMerchant(@PathVariable Long id, @RequestBody MerchantUpdateRequest merchantUpdateRequest) {
        Merchant updatedMerchant = merchantService.partialUpdateMerchant(id, merchantUpdateRequest);
        return ResponseEntity.ok(updatedMerchant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.ok("Merchant deleted successfully");
    }

    private Merchant mapToEntity(MerchantRegistrationRequest merchantRequest) {
        Merchant merchant = new Merchant();
        merchant.setCnpj(merchantRequest.getCnpj());
        merchant.setEmail(merchantRequest.getEmail());
        merchant.setName(merchantRequest.getName());
        merchant.setPassword(merchantRequest.getPassword());
        return merchant;
    }

    private Merchant mapToEntity(MerchantUpdateRequest merchantUpdateRequest) {
        Merchant merchant = new Merchant();
        merchant.setEmail(merchantUpdateRequest.getEmail());
        merchant.setName(merchantUpdateRequest.getName());
        if (merchantUpdateRequest.getPassword() != null) {
            merchant.setPassword(merchantUpdateRequest.getPassword()); // Password encoding is handled in the service layer
        }
        return merchant;
    }
}
