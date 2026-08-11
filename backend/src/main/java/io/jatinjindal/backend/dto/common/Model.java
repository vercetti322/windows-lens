package io.jatinjindal.backend.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Model {
    private Provider provider;
    private String name;
}
