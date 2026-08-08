package io.jatinjindal.backend.store;

import io.jatinjindal.backend.model.ChatSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionStore {

    private final Map<UUID, ChatSession> sessionMap = new HashMap<>();

    public void save(ChatSession session) {
        sessionMap.put(session.getId(), session);
    }

    public Optional<ChatSession> find(UUID sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }
}
