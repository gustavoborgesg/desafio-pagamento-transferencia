package com.pagamento.desafio.pagamento_simplificado.external.exceptions.notification;

import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class NotificationServiceException extends CustomException {
    public NotificationServiceException(String message) {
        super(message, "NOTIFICATION_ERROR", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public NotificationServiceException(String message, Throwable cause) {
        super(message, cause, "NOTIFICATION_ERROR", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
