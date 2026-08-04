package io.jatinjindal.backend.service;

import static io.jatinjindal.backend.constant.BackendConstants.*;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompletionService {

    private final ChatClient chatClient;
    private final OllamaService ollamaService;

    public String getSuggestion(ExplanationRequest request) {
        boolean ollamaStatus = ollamaService.isOllamaRunning();
        if (!ollamaStatus) { ollamaService.startOllama(); }

        String response = chatClient.prompt().user(user -> user.text(COMPLETION_PROMPT)
                .param(LANGUAGE, request.language()).param(BEFORE_CURSOR, request.beforeCursor())
        ).options(OllamaChatOptions.builder().maxTokens(request.maxTokens())
                .temperature(request.temperature()).model(request.model())
        ).call().content();

        return sanitize(Objects.requireNonNull(response), request.beforeCursor());
    }

    private String sanitize(String response, String beforeCursor) {
        if (response.isBlank()) { return ""; }

        // remove Markdown back-ticks
        String raw = response.strip().replaceFirst("^```\\w*\\R?", "")
                .replaceFirst("\\R?```$", "").strip();

        // remove text before/after the cursor
        return removeOverlap(raw, beforeCursor);
    }

    private String removeOverlap(String raw, String beforeCursor) {
        int beforeOverlap = Math.min(beforeCursor.length(), raw.length());
        for (int overlap = beforeOverlap; overlap > 0; overlap--) {
            if (beforeCursor.endsWith(raw.substring(0, overlap))) {
                raw = raw.substring(overlap); break;
            }
        } return raw;
    }
}
