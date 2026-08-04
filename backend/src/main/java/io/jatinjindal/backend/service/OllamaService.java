package io.jatinjindal.backend.service;

import io.jatinjindal.backend.exception.NotepadCopilotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
public class OllamaService {

    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;

    public void startOllama() {
        try { new ProcessBuilder(OLLAMA_PATH, OLLAMA_SERVE)
                .redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new NotepadCopilotException(OLLAMA_FAILED_TO_START, e);
        }
    }

    public boolean isOllamaRunning() {
        try { HttpURLConnection connection = (HttpURLConnection)
                URI.create(ollamaBaseUrl + OLLAMA_TAGS_URI).toURL().openConnection();

            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) { return false; }
    }
}
