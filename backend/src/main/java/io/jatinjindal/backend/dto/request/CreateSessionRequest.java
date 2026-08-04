package io.jatinjindal.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSessionRequest {

    @NotBlank(message = "Selected text cannot be blank")
    @Size(min = 5, max = 10000)
    private String selectedText;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "User Message cannot be blank")
    @Size(min = 5, max = 10000)
    private String userMessage;
}
