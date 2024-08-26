package com.pagamento.desafio.pagamento_simplificado.domain.services;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;

import java.util.List;

public interface MerchantService {
    Merchant registerMerchant(Merchant merchant);

    Merchant getMerchantById(Long id);

    List<Merchant> getAllMerchants();

    Merchant updateMerchant(Long id, Merchant merchant);

    Merchant partialUpdateMerchant(Long id, MerchantUpdateRequest merchantUpdateRequest);

    void deleteMerchant(Long id);
}
