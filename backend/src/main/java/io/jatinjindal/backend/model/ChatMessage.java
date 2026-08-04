package io.jatinjindal.backend.model;

import io.jatinjindal.backend.dto.common.MessageRole;
import lombok.Builder;
import java.time.Instant;

@Builder
public record ChatMessage(
    MessageRole role,
    String content,
    Instant timestamp
) { }
