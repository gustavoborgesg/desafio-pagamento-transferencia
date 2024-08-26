package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.domain.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Transfer> executeTransfer(@RequestBody TransferRequest transferRequest) {
        Transfer transfer = transferService.executeTransfer(transferRequest);
        return ResponseEntity.ok(transfer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        Transfer transfer = transferService.getTransferById(id);
        return ResponseEntity.ok(transfer);
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
