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
                () -> new WindowsLensException("Model not found")
        );
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
