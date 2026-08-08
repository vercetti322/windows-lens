package io.jatinjindal.backend.service;

import io.jatinjindal.backend.exception.WindowsLensException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
public class ModelProvider {

    public String chat(String prompt, String model) {
        String provider = getProvider(model).orElseThrow(
                () -> new WindowsLensException(MODEL_NOT_FOUND_ERROR)
        );

        if (provider.equals(GEMINI)) { return geminiChat(prompt, model); }
        else if (provider.equals(OLLAMA)) { return ollamaChat(prompt, model); }

        else { throw new WindowsLensException(UNKNOWN_PROVIDER_ERROR); }
    }

    private String geminiChat(String prompt, String model) {
        return "Gemini response";
    }

    private String ollamaChat(String prompt, String model) {
        return "Ollama response";
    }

    private Optional<String> getProvider(String model) {
        Path modelsPath = Paths.get(System.getProperty(USER_HOME))
                .resolve(MODEL_LIST_PATH);

        if (!Files.exists(modelsPath)) { return Optional.empty(); }
        try {
            return Files.readAllLines(modelsPath).stream()
                    .map(String::trim).filter(l -> l.startsWith(model + "="))
                    .map(l -> l.split("=", 2)[1]).findFirst();
        } catch (IOException e) { return Optional.empty(); }
    }
}
