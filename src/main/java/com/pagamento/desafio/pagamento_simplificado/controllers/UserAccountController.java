package com.pagamento.desafio.pagamento_simplificado.controllers;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.deposit.DepositRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.services.UserAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping("/deposit")
    public ResponseEntity<String> depositToWallet(@RequestBody @Valid DepositRequest depositRequest) {
        userAccountService.depositToWallet(depositRequest.getUserId(), depositRequest.getAmount());
        return ResponseEntity.ok("Deposit successful");
    }
}
