package com.pagamento.desafio.pagamento_simplificado.external.services;

import com.pagamento.desafio.pagamento_simplificado.external.dtos.NotificationRequest;

public interface TransferRestClient {
    void authorizeTransfer();

    void notifyPayee(NotificationRequest notificationRequest);
}
