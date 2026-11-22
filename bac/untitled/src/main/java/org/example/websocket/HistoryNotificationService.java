package org.example.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;

import java.util.UUID;

@ApplicationScoped
public class HistoryNotificationService {

    private static final String HISTORY_UPDATED_MESSAGE =
            "{\"type\":\"HISTORY_UPDATED\"}";

    @Inject
    private WebSocketSessionRegistry webSocketSessionRegistry;

    @Inject
    private TransactionSynchronizationRegistry transactionRegistry;

    public void notifyHistoryUpdated(UUID userId) {

        int transactionStatus = transactionRegistry.getTransactionStatus();

        if (transactionStatus == Status.STATUS_ACTIVE || transactionStatus == Status.STATUS_MARKED_ROLLBACK) {

            transactionRegistry.registerInterposedSynchronization(new Synchronization() {
                        @Override
                        public void beforeCompletion() {}

                        @Override
                        public void afterCompletion(int status) {

                            if (status == Status.STATUS_COMMITTED) {
                                sendNotification(userId);
                            }
                        }
                    }
            );

        } else if (transactionStatus == Status.STATUS_NO_TRANSACTION) {

            sendNotification(userId);
        }
    }

    private void sendNotification(UUID userId) {
        webSocketSessionRegistry.sendToUser(
                userId,
                HISTORY_UPDATED_MESSAGE
        );
    }
}