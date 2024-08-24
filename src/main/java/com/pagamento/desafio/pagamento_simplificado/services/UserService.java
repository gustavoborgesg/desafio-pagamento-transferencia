package com.pagamento.desafio.pagamento_simplificado.services;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.ClientRegistrationRequest;
import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.MerchantRegistrationRequest;

public interface UserService {
    void registerClient(ClientRegistrationRequest clientRequest);

    void registerMerchant(MerchantRegistrationRequest merchantRequest);
}
