package io.jatinjindal.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChatSession {
    private UUID sessionId;
    private String selectedText;
    private List<ChatMessage> messages;
}
