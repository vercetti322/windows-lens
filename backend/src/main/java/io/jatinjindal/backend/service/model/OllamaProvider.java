package io.jatinjindal.backend.service.model;

import org.springframework.stereotype.Component;

@Component
public class OllamaProvider implements ModelProvider {

    @Override
    public String chat(String prompt, String model) {
        return "";
    }
}
