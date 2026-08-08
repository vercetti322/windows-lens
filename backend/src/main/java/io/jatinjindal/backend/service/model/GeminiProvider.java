package io.jatinjindal.backend.service.model;

import org.springframework.stereotype.Service;

@Service
public class GeminiProvider implements ModelProvider {

    @Override
    public String chat(String prompt, String model) {
        return "";
    }
}
