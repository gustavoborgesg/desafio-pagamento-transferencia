package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantDefaultResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.domain.services.MerchantService;
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
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public ResponseEntity<MerchantDefaultResponse> registerMerchant(@RequestBody MerchantRegistrationRequest merchantRequest) {
        Merchant merchant = mapToEntity(merchantRequest);
        Merchant savedMerchant = merchantService.registerMerchant(merchant);
        return ResponseEntity.ok(mapToResponse(savedMerchant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDefaultResponse> getMerchantById(@PathVariable Long id) {
        Merchant merchant = merchantService.getMerchantById(id);
        return ResponseEntity.ok(mapToResponse(merchant));
    }

    @GetMapping
    public ResponseEntity<List<MerchantDefaultResponse>> getAllMerchants() {
        List<MerchantDefaultResponse> merchants = merchantService.getAllMerchants().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(merchants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantDefaultResponse> updateMerchant(@PathVariable Long id, @RequestBody MerchantRegistrationRequest merchantRequest) {
        Merchant updatedMerchant = merchantService.updateMerchant(id, mapToEntity(merchantRequest));
        return ResponseEntity.ok(mapToResponse(updatedMerchant));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MerchantDefaultResponse> partialUpdateMerchant(@PathVariable Long id, @RequestBody MerchantUpdateRequest merchantUpdateRequest) {
        Merchant updatedMerchant = merchantService.partialUpdateMerchant(id, merchantUpdateRequest);
        return ResponseEntity.ok(mapToResponse(updatedMerchant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.ok("Merchant deleted successfully");
    }

    private Merchant mapToEntity(MerchantRegistrationRequest merchantRequest) {
        Merchant merchant = new Merchant();
        merchant.setCnpj(merchantRequest.getCnpj());
        merchant.setName(merchantRequest.getName());
        merchant.setEmail(merchantRequest.getEmail());
        merchant.setPassword(merchantRequest.getPassword());
        merchant.getWallet().setBalance(merchantRequest.getInitialBalance());
        return merchant;
    }

    private MerchantDefaultResponse mapToResponse(Merchant merchant) {
        MerchantDefaultResponse response = new MerchantDefaultResponse();
        response.setId(merchant.getId());
        response.setCnpj(merchant.getCnpj());
        response.setName(merchant.getName());
        response.setEmail(merchant.getEmail());
        response.setBalance(merchant.getWallet().getBalance());
        return response;
    }
}
