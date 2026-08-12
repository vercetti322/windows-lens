package io.jatinjindal.backend.service;

import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.dto.common.Model;
import io.jatinjindal.backend.exception.WindowsLensException;
import io.jatinjindal.backend.store.ModelStore;
import io.jatinjindal.backend.transmitter.GoogleTransmitter;
import io.jatinjindal.backend.transmitter.OllamaTransmitter;
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
import java.util.ArrayList;
import java.util.List;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ModelProvider {

    private final GoogleGenAiChatModel geminiProvider;
    private final OllamaChatModel ollamaProvider;
    private final GoogleTransmitter googleTransmitter;
    private final OllamaTransmitter ollamaTransmitter;
    private final ModelStore modelStore;

    public String chat(String prompt, String model, Provider provider) {
        if (provider.equals(Provider.GEMINI)) {
            return geminiChat(prompt, model);
        } else { return ollamaChat(prompt, model); }
    }

    public List<Model> fetchAvailableModels(boolean gemini, boolean ollama) {
        List<Model> models = new ArrayList<>();
        if (gemini) { models.addAll(googleTransmitter.getModels()); }

        ensureOllamaRunning();
        if (ollama) { models.addAll(ollamaTransmitter.getModels()); }
        
        modelStore.saveAll(models); return models;
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
