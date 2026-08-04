package io.jatinjindal.backend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChatSession {

    @NotBlank(message = "Session ID cannot be blank")
    private UUID sessionId;

    @NotBlank(message = "Selected Text cannot be blank")
    private String selectedText;

    @NotEmpty(message = "messages cannot be empty")
    private List<@Valid ChatMessage> messages;
}
