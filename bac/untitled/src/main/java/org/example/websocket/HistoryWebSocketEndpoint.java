package org.example.websocket;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.UUID;

@ServerEndpoint(
        value = "/ws/history",
        configurator = HistoryWebSocketEndpoint.HttpSessionConfigurator.class
)
public class HistoryWebSocketEndpoint {

    private static final String HTTP_SESSION_KEY = "httpSession";
    private static final String USER_ID_ATTRIBUTE = "userId";

    @Inject
    private WebSocketSessionRegistry webSocketSessionRegistry;

    private UUID userId;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

        HttpSession httpSession = (HttpSession)
                session.getUserProperties().get(HTTP_SESSION_KEY);

        if (httpSession == null) {
            closeSession(session, "HTTP session not found");
            return;
        }

        Object userIdObject = httpSession.getAttribute(USER_ID_ATTRIBUTE);

        if (userIdObject == null) {
            closeSession(session, "User is not authenticated");
            return;
        }

        try {
            userId = userIdObject instanceof UUID
                    ? (UUID) userIdObject
                    : UUID.fromString(userIdObject.toString());
        } catch (IllegalArgumentException e) {
            closeSession(session, "Invalid user ID");
            return;
        }

        webSocketSessionRegistry.register(userId, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if (userId != null) {
            webSocketSessionRegistry.unregister(userId, session);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        if (userId != null) {
            webSocketSessionRegistry.unregister(userId, session);
        }
    }

    private void closeSession(Session session, String reason) {
        try {
            session.close(
                    new CloseReason(
                            CloseReason.CloseCodes.VIOLATED_POLICY,
                            reason
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class HttpSessionConfigurator
            extends ServerEndpointConfig.Configurator {

        @Override
        public void modifyHandshake(
                ServerEndpointConfig config,
                HandshakeRequest request,
                HandshakeResponse response
        ) {
            config.getUserProperties().put(
                    HTTP_SESSION_KEY,
                    request.getHttpSession()
            );
        }
    }
}