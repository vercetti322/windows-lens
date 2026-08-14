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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ModelProvider {

    private final ObjectProvider<GoogleGenAiChatModel> geminiProvider;
    private final ObjectProvider<OllamaChatModel> ollamaProvider;
    private final ModelStore modelStore;
    private final GoogleTransmitter googleTransmitter;
    private final OllamaTransmitter ollamaTransmitter;

    public String chat(String prompt, String model, Provider provider) {
        if (provider.equals(Provider.GEMINI)) {
            return geminiChat(prompt, model);
        } else { return ollamaChat(prompt, model); }
    }

    public List<Model> fetchAvailableModels(boolean gemini, boolean ollama) {
        List<Model> models = new ArrayList<>();
        if (gemini) { models.addAll(googleTransmitter.getModels()); }

        ollamaTransmitter.ensureOllamaRunning();
        String port = ollamaTransmitter.fetchPort().orElseThrow(
                () -> new WindowsLensException(OLLAMA_SETTINGS_ERROR)
        );

        if (ollama) { models.addAll(ollamaTransmitter.getModels(port)); }
        modelStore.saveAll(models); return models;
    }

    private String geminiChat(String prompt, String model) {
        var geminiModel = geminiProvider.getObject();
        Prompt request = Prompt.builder().messages(new UserMessage(prompt),
                        new SystemMessage(SYSTEM_PROMPT)
                ).chatOptions(GoogleGenAiChatOptions
                        .builder().model(model).build()).build();

        ChatResponse response = geminiModel.call(request);
        if (response.getResult() == null) {
            throw new WindowsLensException(GEMINI_RESPONSE_ERROR);
        }

        return response.getResult().getOutput().getText();
    }

    private String ollamaChat(String prompt, String model) {
        var ollamaModel = ollamaProvider.getObject();
        Prompt request = Prompt.builder().messages(new UserMessage(prompt),
                        new SystemMessage(SYSTEM_PROMPT)
                ).chatOptions(OllamaChatOptions
                        .builder().model(model).build()).build();

        ChatResponse response = ollamaModel.call(request);
        if (response.getResult() == null) {
            throw new WindowsLensException(OLLAMA_RESPONSE_ERROR);
        }

        return response.getResult().getOutput().getText();
    }
}
