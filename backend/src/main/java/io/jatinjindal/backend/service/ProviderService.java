package io.jatinjindal.backend.service;

import com.google.genai.Client;
import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.dto.request.ProviderRequest;
import io.jatinjindal.backend.transmitter.GoogleTransmitter;
import io.jatinjindal.backend.transmitter.OllamaTransmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final GoogleTransmitter googleTransmitter;
    private final OllamaTransmitter ollamaTransmitter;
    private final ConfigurableListableBeanFactory beanFactory;
    private final CredentialsProvider credentialsProvider;

    public String validate(ProviderRequest request) {
        return switch (request.getProvider()) {
            case GEMINI -> {
                var models = googleTransmitter.getModels(request.getValue());
                if (models.isEmpty()) { yield "No"; }

                registerGeminiBean(request.getValue());
                credentialsProvider.put(Provider.GEMINI, request.getValue()); yield "Ok";
            }
            case OLLAMA -> {
                ollamaTransmitter.ensureOllamaRunning();
                var models = ollamaTransmitter.getModels(request.getValue());

                if (models.isEmpty()) { yield "No"; }
                registerOllamaBean(request.getValue());

                ollamaTransmitter.storePort(request.getValue()); yield "Ok";
            }
        };
    }

    private void registerOllamaBean(String port) {
        OllamaApi api = OllamaApi.builder().baseUrl(
                LOCALHOST + port).build();

        var model = OllamaChatModel.builder().ollamaApi(api).build();
        beanFactory.registerSingleton(OLLAMA_BEAN, model);
    }

    private void registerGeminiBean(String encodedApiKey) {
        String apiKey = new String(Base64.getDecoder().decode(
                encodedApiKey), StandardCharsets.UTF_8
        );

        var model = GoogleGenAiChatModel.builder().genAiClient(
                Client.builder().apiKey(apiKey).build()).build();

        beanFactory.registerSingleton(GEMINI_BEAN, model);
    }
}
