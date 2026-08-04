package io.jatinjindal.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSessionRequest {

    @NotBlank(message = "Selected text cannot be blank")
    private String selectedText;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "User Message cannot be blank")
    private String userMessage;
}
