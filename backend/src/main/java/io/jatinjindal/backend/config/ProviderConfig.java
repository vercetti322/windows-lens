package io.jatinjindal.backend.config;

import com.google.genai.Client;
import io.jatinjindal.backend.exception.WindowsLensException;
import io.jatinjindal.backend.service.CredentialsProvider;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static io.jatinjindal.backend.constant.BackendConstants.*;

@Configuration
public class ProviderConfig {

    @Bean(name = "geminiProvider")
    public GoogleGenAiChatModel geminiProvider(CredentialsProvider provider) {
        String apiKey = provider.get(GEMINI_API_KEY).orElseThrow(
                () -> new WindowsLensException(GEMINI_KEY_NOT_FOUND)
        );

        return GoogleGenAiChatModel.builder().genAiClient(
                Client.builder().apiKey(apiKey).build()).build();
    }

    @Bean(name = "ollamaProvider")
    public OllamaChatModel ollamaProvider() {
        OllamaApi api = OllamaApi.builder().baseUrl(OLLAMA_BASE_URL).build();
        return OllamaChatModel.builder().ollamaApi(api).build();
    }
}
