package com.pagamento.desafio.pagamento_simplificado.external.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.external.dtos.AuthorizationResponse;
import com.pagamento.desafio.pagamento_simplificado.external.dtos.NotificationRequest;
import com.pagamento.desafio.pagamento_simplificado.external.exceptions.authorization.AuthorizationServiceException;
import com.pagamento.desafio.pagamento_simplificado.external.exceptions.notification.NotificationServiceException;
import com.pagamento.desafio.pagamento_simplificado.external.services.TransferRestClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TransferRestClientImpl implements TransferRestClient {

    private final RestClient restClient = RestClient.create();
    private static final Logger logger = LoggerFactory.getLogger(TransferRestClientImpl.class);

    @Value("${transfer.authorization.url}")
    private String authorizationUrl;

    @Value("${transfer.notification.url}")
    private String notificationUrl;

    @Override
    public void authorizeTransfer() {
        try {
            restClient.get()
                    .uri(authorizationUrl)
                    .retrieve()
                    .body(AuthorizationResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthorizationServiceException("Transfer not authorized", e);
            }
            throw e;
        } catch (Exception e) {
            throw new AuthorizationServiceException("Failed to receive a valid response from the authorization service", e);
        }
    }

    @Override
    public void notifyPayee(NotificationRequest notificationRequest) {
        try {
            restClient.post()
                    .uri(notificationUrl)
                    .body(notificationRequest)
                    .retrieve()
                    .toEntity(Void.class);
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
                logger.warn("Notification service is not available, try again later: {}", e.getMessage());
            } else {
                throw new NotificationServiceException("Failed to send notification to payee", e);
            }
        } catch (Exception e) {
            throw new NotificationServiceException("Failed to send notification to payee", e);
        }
    }
}
