package io.jatinjindal.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ChatMessage(

    @NotNull(message = "Role cannot be null")
    MessageRole role,

    @NotBlank(message = "Content cannot be blank")
    String content,

    @NotNull(message = "Timestamp cannot be null")
    Instant timestamp
) { }
