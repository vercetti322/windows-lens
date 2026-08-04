package io.jatinjindal.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChatSession {
    private UUID id;
    private String selectedText;
    private String model;
    private List<ChatMessage> messages;
}
