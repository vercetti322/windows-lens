package io.jatinjindal.backend.model;

import java.time.Instant;

public record ChatMessage(
    MessageRole role,
    String content,
    Instant timestamp
) { }
