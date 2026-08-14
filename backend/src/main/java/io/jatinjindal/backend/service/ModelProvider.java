package io.jatinjindal.backend.service;

import com.google.genai.Client;
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
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ModelProvider {

    private final CredentialsProvider credentialsProvider;
    private final ModelStore modelStore;
    private final GoogleTransmitter googleTransmitter;
    private final OllamaTransmitter ollamaTransmitter;

    public String chat(String prompt, String model, Provider provider) {
        return switch (provider) {
            case GEMINI -> geminiChat(prompt, model);
            case OLLAMA -> ollamaChat(prompt, model);
        };
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
        String apiKey = credentialsProvider.get(GEMINI_API_KEY)
                .orElseThrow(() -> new WindowsLensException(GEMINI_KEY_NOT_FOUND));

        var provider = GoogleGenAiChatModel.builder().genAiClient(
                Client.builder().apiKey(apiKey).build()).build();

        Prompt request = Prompt.builder().messages(
                new UserMessage(prompt), new SystemMessage(SYSTEM_PROMPT)
        ).chatOptions(GoogleGenAiChatOptions
                .builder().model(model).build()).build();

        ChatResponse response = provider.call(request);

        if (response.getResult() == null) {
            throw new WindowsLensException(GEMINI_RESPONSE_ERROR);
        } return response.getResult().getOutput().getText();
    }

    private String ollamaChat(String prompt, String model) {
        String port = credentialsProvider.get(OLLAMA_PORT)
                .orElseThrow(() -> new WindowsLensException(OLLAMA_PORT_NOT_FOUND));

        var api = OllamaApi.builder().baseUrl(LOCALHOST + port).build();
        var provider = OllamaChatModel.builder().ollamaApi(api).build();

        Prompt request = Prompt.builder().messages(
                new UserMessage(prompt), new SystemMessage(SYSTEM_PROMPT)
        ).chatOptions(OllamaChatOptions
                .builder().model(model).build()).build();

        ChatResponse response = provider.call(request);

        if (response.getResult() == null) {
            throw new WindowsLensException(OLLAMA_RESPONSE_ERROR);
        } return response.getResult().getOutput().getText();
    }
}
