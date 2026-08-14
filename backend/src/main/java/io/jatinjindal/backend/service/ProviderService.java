package io.jatinjindal.backend.service;

import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.dto.request.ProviderRequest;
import io.jatinjindal.backend.transmitter.GoogleTransmitter;
import io.jatinjindal.backend.transmitter.OllamaTransmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final GoogleTransmitter googleTransmitter;
    private final OllamaTransmitter ollamaTransmitter;
    private final CredentialsProvider credentialsProvider;

    public String validate(ProviderRequest request) {
        return switch (request.getProvider()) {
            case GEMINI -> {
                var models = googleTransmitter.getModels(request.getValue());
                if (models.isEmpty()) { yield "No"; }

                credentialsProvider.put(Provider.GEMINI, request.getValue()); yield "Ok";
            }
            case OLLAMA -> {
                ollamaTransmitter.ensureOllamaRunning();
                var models = ollamaTransmitter.getModels(request.getValue());

                if (models.isEmpty()) { yield "No"; }
                ollamaTransmitter.storePort(request.getValue()); yield "Ok";
            }
        };
    }
}
