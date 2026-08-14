package io.jatinjindal.backend.transmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jatinjindal.backend.dto.common.Model;
import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.exception.WindowsLensException;
import io.jatinjindal.backend.service.CredentialsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.StreamSupport;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Component
@RequiredArgsConstructor
public class GoogleTransmitter {

    private final CredentialsProvider provider;
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Model> getModels() {
        String apiKey = provider.get(GEMINI_API_KEY).orElseThrow(
                () -> new WindowsLensException(GEMINI_KEY_NOT_FOUND)
        ); return getModels(apiKey);
    }

    public List<Model> getModels(String encodedApiKey) {
        String apiKey = new String(Base64.getDecoder().decode(
                encodedApiKey), StandardCharsets.UTF_8
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(GOOGLE_GEMINI_MODELS_URI)
                .header(API_KEY_HEADER, apiKey).GET().build();

        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new WindowsLensException(GEMINI_MODELS_RESPONSE_ERROR);
            } return formatModelResponse(response.body());
        } catch (IOException | InterruptedException e) {
            throw new WindowsLensException(GEMINI_MODELS_RESPONSE_ERROR, e);
        }
    }

    private List<Model> formatModelResponse(String body) {
        try {
            JsonNode root = mapper.readTree(body);
            List<String> models = StreamSupport.stream(root.path(MODELS).spliterator(), false)
                    .map(model -> model.path(NAME).asText())
                    .map(name -> name.substring(7)).skip(5).toList();

            return models.stream().map(model -> Model.builder()
                    .provider(Provider.GEMINI).name(model).build()).toList();
        } catch (JsonProcessingException e) {
            throw new WindowsLensException(GEMINI_MODELS_RESPONSE_ERROR, e);
        }
    }
}
