package io.jatinjindal.backend.dto.request;

import io.jatinjindal.backend.model.ModelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSessionRequest {

    @NotBlank(message = "Selected text cannot be blank")
    private String selectedText;

    @NotNull(message = "Model Type cannot be blank")
    private ModelType modelType;
}
