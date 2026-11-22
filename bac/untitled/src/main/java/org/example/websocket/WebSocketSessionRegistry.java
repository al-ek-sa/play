package org.example.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class WebSocketSessionRegistry {

    private final ConcurrentHashMap<UUID, Set<Session>> sessions =
            new ConcurrentHashMap<>();

    public void register(UUID userId, Session session) {
        sessions.computeIfAbsent(
                userId,
                ignored -> ConcurrentHashMap.newKeySet()
        ).add(session);
    }

    public void unregister(UUID userId, Session session) {
        Set<Session> userSessions = sessions.get(userId);

        if (userSessions == null) {
            return;
        }

        userSessions.remove(session);

        if (userSessions.isEmpty()) {
            sessions.remove(userId, userSessions);
        }
    }

    public void sendToUser(UUID userId, String message) {
        Set<Session> userSessions = sessions.get(userId);

        if (userSessions == null || userSessions.isEmpty()) {
            return;
        }

        for (Session session : userSessions) {
            if (!session.isOpen()) {
                unregister(userId, session);
                continue;
            }

            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception exception) {
                unregister(userId, session);
            }
        }
    }

    public int getConnectionCount(UUID userId) {
        Set<Session> userSessions = sessions.get(userId);

        return userSessions == null ? 0 : userSessions.size();
    }
}