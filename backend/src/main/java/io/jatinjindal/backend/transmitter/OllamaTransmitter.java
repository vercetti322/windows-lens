package io.jatinjindal.backend.transmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jatinjindal.backend.dto.common.Model;
import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.exception.WindowsLensException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.StreamSupport;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Component
public class OllamaTransmitter {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Model> getModels() {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(OLLAMA_BASE_URL + TAGS_PATH)
        ).GET().build();

        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new WindowsLensException(OLLAMA_MODELS_RESPONSE_ERROR);
            } return formatModelResponse(response.body());
        } catch (IOException | InterruptedException e) {
            throw new WindowsLensException(OLLAMA_MODELS_RESPONSE_ERROR, e);
        }
    }

    private List<Model> formatModelResponse(String body) {
        try {
            JsonNode root = mapper.readTree(body);
            List<String> models = StreamSupport.stream(root.path(MODELS)
                            .spliterator(), false
                    ).map(model -> model.path(NAME).asText()).toList();

            return models.stream().map(model -> Model.builder()
                    .provider(Provider.OLLAMA).name(model).build()).toList();
        } catch (JsonProcessingException e) {
            throw new WindowsLensException(OLLAMA_MODELS_RESPONSE_ERROR, e);
        }
    }
}
