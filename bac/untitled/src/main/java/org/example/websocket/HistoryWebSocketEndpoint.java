package org.example.websocket;

import com.example.websocket.WebSocketSessionRegistry;
import jakarta.inject.Inject;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/history")
public class HistoryWebSocketEndpoint {

    @Inject
    private WebSocketSessionRegistry webSocketSessionRegistry;
}
