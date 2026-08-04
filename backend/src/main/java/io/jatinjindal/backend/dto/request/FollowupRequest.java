package io.jatinjindal.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FollowupRequest {

    @NotNull(message = "Session ID cannot be null")
    private UUID id;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotBlank(message = "Model cannot be blank")
    private String model;
}
