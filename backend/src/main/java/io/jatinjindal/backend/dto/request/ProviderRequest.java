package io.jatinjindal.backend.dto.request;

import io.jatinjindal.backend.dto.common.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProviderRequest {

    @NotNull(message = "Provider should not be null")
    private Provider provider;

    @NotBlank(message = "Value should not be blank")
    private String value;
}
