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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Component
public class OllamaTransmitter {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Model> getModels(String port) {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(LOCALHOST + port + TAGS_PATH)
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

    public void ensureOllamaRunning() {
        try { Process process = new ProcessBuilder(OLLAMA, PS)
                .redirectErrorStream(true).start();

            if (process.waitFor() == 0) { return; }
            new ProcessBuilder(OLLAMA, SERVE)
                    .redirectErrorStream(true).start();
        } catch (InterruptedException | IOException e) {
            throw new WindowsLensException(OLLAMA_START_ERROR, e);
        }
    }

    public Optional<String> fetchPort() {
        Path ollamaSettings = Paths.get(System.getProperty(USER_HOME),
                WINDOWS_LENS, OLLAMA_SETTINGS_FILE
        );

        if (Files.notExists(ollamaSettings)) {
            throw new WindowsLensException(OLLAMA_SETTINGS_ERROR);
        }

        try {
            String port = Files.readString(ollamaSettings).substring(
                    (OLLAMA_PORT + "=").length()).trim();

            return port.isBlank() ? Optional.empty() : Optional.of(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void storePort(String port) {
        if (Integer.parseInt(port) < 1 || Integer.parseInt(port) > 65535) {
            throw new WindowsLensException(OLLAMA_PORT_ERROR);
        }

        Path root = Paths.get(System.getProperty(USER_HOME), WINDOWS_LENS);
        try {
            Files.createDirectories(root);
            Path ollamaSettings = root.resolve(OLLAMA_SETTINGS_FILE);

            Files.writeString(ollamaSettings, OLLAMA_PORT + "=" +
                    port + System.lineSeparator()
            );
        } catch (IOException e) {
            throw new WindowsLensException(OLLAMA_PORT_ERROR, e);
        }
    }
}
