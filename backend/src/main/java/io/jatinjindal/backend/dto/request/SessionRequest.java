package io.jatinjindal.backend.dto.request;

import io.jatinjindal.backend.dto.common.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SessionRequest {

    @NotBlank(message = "Selected text cannot be blank")
    @Size(min = 5, max = 10000)
    private String selectedText;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotNull(message = "Provider cannot be null")
    private Provider provider;

    @NotBlank(message = "User Message cannot be blank")
    @Size(min = 5, max = 10000)
    private String userMessage;
}
