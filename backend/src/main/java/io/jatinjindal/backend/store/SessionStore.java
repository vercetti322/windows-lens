package io.jatinjindal.backend.store;

import io.jatinjindal.backend.model.ChatSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {

    private final Map<UUID, ChatSession> sessionMap = new ConcurrentHashMap<>();

    public void save(ChatSession session) {
        sessionMap.put(session.getId(), session);
    }

    public Optional<ChatSession> find(UUID sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }
}
