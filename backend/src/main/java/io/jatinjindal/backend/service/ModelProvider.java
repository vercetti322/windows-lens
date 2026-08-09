package io.jatinjindal.backend.service;

import io.jatinjindal.backend.exception.WindowsLensException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ModelProvider {

    private final GoogleGenAiChatModel geminiProvider;
    private final OllamaChatModel ollamaProvider;

    public String chat(String prompt, String model) {
        String provider = getProvider(model).orElseThrow(
                () -> new WindowsLensException(MODEL_NOT_FOUND_ERROR)
        );

        if (provider.equals(GEMINI)) { return geminiChat(prompt, model); }
        else if (provider.equals(OLLAMA)) { return ollamaChat(prompt, model); }

        else { throw new WindowsLensException(UNKNOWN_PROVIDER_ERROR); }
    }

    private String geminiChat(String prompt, String model) {
        Prompt request = Prompt.builder().messages(new UserMessage(prompt),
                        new SystemMessage(SYSTEM_PROMPT)
                ).chatOptions(GoogleGenAiChatOptions
                        .builder().model(model).build()).build();

        ChatResponse response = geminiProvider.call(request);
        if (response.getResult() == null) {
            throw new WindowsLensException(GEMINI_RESPONSE_ERROR);
        }

        return response.getResult().getOutput().getText();
    }

    private String ollamaChat(String prompt, String model) {
        ensureOllamaRunning();
        Prompt request = Prompt.builder().messages(new UserMessage(prompt),
                        new SystemMessage(SYSTEM_PROMPT)
                ).chatOptions(OllamaChatOptions
                        .builder().model(model).build()).build();

        ChatResponse response = ollamaProvider.call(request);
        if (response.getResult() == null) {
            throw new WindowsLensException(OLLAMA_RESPONSE_ERROR);
        }

        return response.getResult().getOutput().getText();
    }

    private Optional<String> getProvider(String model) {
        Path modelsPath = Paths.get(System.getProperty(USER_HOME))
                .resolve(MODEL_LIST_PATH);

        if (!Files.exists(modelsPath)) { return Optional.empty(); }
        try {
            return Files.readAllLines(modelsPath).stream()
                    .map(String::trim).filter(l -> l.startsWith(model + "<=>"))
                    .map(l -> l.split("<=>", 2)[1]).findFirst();
        } catch (IOException e) { return Optional.empty(); }
    }

    private void ensureOllamaRunning() {
        try { Process process = new ProcessBuilder(OLLAMA, PS)
                .redirectErrorStream(true).start();

            if (process.waitFor() == 0) { return; }

            new ProcessBuilder(OLLAMA, SERVE)
                    .redirectErrorStream(true).start();
        } catch (InterruptedException | IOException e) {
            throw new WindowsLensException(OLLAMA_START_ERROR, e);
        }
    }
}
