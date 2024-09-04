package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferDefaultResponse;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.transfer.TransferRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Transfer;
import com.pagamento.desafio.pagamento_simplificado.domain.services.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferDefaultResponse> executeTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        Transfer transfer = transferService.executeTransfer(transferRequest);
        return ResponseEntity.ok(mapToResponse(transfer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDefaultResponse> getTransferById(@PathVariable Long id) {
        Transfer transfer = transferService.getTransferById(id);
        return ResponseEntity.ok(mapToResponse(transfer));
    }

    @GetMapping
    public ResponseEntity<List<TransferDefaultResponse>> getAllTransfers() {
        List<TransferDefaultResponse> transfers = transferService.getAllTransfers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transfers);
    }

    private TransferDefaultResponse mapToResponse(Transfer transfer) {
        TransferDefaultResponse response = new TransferDefaultResponse();
        response.setId(transfer.getId());
        response.setPayerId(transfer.getPayer().getId());
        response.setPayeeId(transfer.getPayee().getId());
        response.setAmount(transfer.getAmount());
        response.setTimestamp(transfer.getTimestamp());
        response.setStatus(transfer.getStatus());
        return response;
    }
}
