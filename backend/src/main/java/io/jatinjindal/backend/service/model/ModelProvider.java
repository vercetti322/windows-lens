package io.jatinjindal.backend.service.model;

import io.jatinjindal.backend.model.ChatSession;

public interface ModelProvider {

    String chat(ChatSession session);
}
