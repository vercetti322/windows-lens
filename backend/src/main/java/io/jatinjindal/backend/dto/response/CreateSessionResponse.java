package io.jatinjindal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateSessionResponse {
    private UUID sessionId;
    private String response;
}
