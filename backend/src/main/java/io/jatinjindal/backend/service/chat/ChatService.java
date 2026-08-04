package io.jatinjindal.backend.service.chat;

import io.jatinjindal.backend.dto.common.MessageRole;
import io.jatinjindal.backend.dto.request.CreateSessionRequest;
import io.jatinjindal.backend.dto.request.FollowupRequest;
import io.jatinjindal.backend.dto.response.CreateSessionResponse;
import io.jatinjindal.backend.dto.response.FollowupResponse;
import io.jatinjindal.backend.exception.WindowsLensException;
import io.jatinjindal.backend.model.ChatMessage;
import io.jatinjindal.backend.model.ChatSession;
import io.jatinjindal.backend.service.model.ModelProvider;
import io.jatinjindal.backend.store.SessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SessionStore store;
    private final ModelProvider modelProvider;

    public CreateSessionResponse createSession(CreateSessionRequest request) {
        if (!validateModel(request.getModel())) {
            throw new WindowsLensException(MODEL_NOT_FOUND_ERROR);
        }

        var session = transformToSession(request); store.save(session);
        try {
            String response = modelProvider.chat(session.prompt());
            addChatMessage(MessageRole.ASSISTANT, response, session.getId());

            return CreateSessionResponse.builder().id(session.getId())
                    .response(response).build();   
        } catch (Exception e) {
            throw new WindowsLensException(MODEL_RESPONSE_ERROR, e);
        }
    }

    private boolean validateModel(String model) {
        Path modelsPath = Paths.get(System.getProperty(USER_HOME))
                .resolve(MODEL_LIST_PATH);

        if (!Files.exists(modelsPath)) { return false; }
        try {
            return Files.readAllLines(modelsPath).stream()
                    .map(String::trim).filter(s -> !s.isEmpty())
                    .anyMatch(model::equals);
        } catch (IOException e) { return false; }
    }

    private ChatSession transformToSession(CreateSessionRequest request) {
        String model = request.getModel();
        String selectedText = request.getSelectedText();

        String userMessage = request.getUserMessage();
        var chatMessage = ChatMessage.builder().role(MessageRole.USER)
                .content(userMessage).build();

        return ChatSession.builder().id(UUID.randomUUID())
                .selectedText(selectedText).model(model)
                .messages(List.of(chatMessage)).build();
    }

    public FollowupResponse sendFollowup(FollowupRequest request) {
        ChatSession session = store.find(request.getId()).orElseThrow(
                () -> new WindowsLensException(SESSION_NOT_FOUND_ERROR)
        );

        if (session.getMessages().stream().filter(m -> m.role()
                == MessageRole.USER).count() >= FOLLOWUP_LIMIT
        ) {
            throw new WindowsLensException(SESSION_MAX_PROMPTS_ERROR);
        }

        String userMessage = request.getMessage();
        addChatMessage(MessageRole.USER, userMessage, session.getId());

        boolean sessionEnded = session.getMessages().size() >= FOLLOWUP_LIMIT;
        try {
            String response = modelProvider.chat(session.prompt());
            addChatMessage(MessageRole.ASSISTANT, response, session.getId());

            return FollowupResponse.builder().response(response)
                    .sessionEnded(sessionEnded).build();
        } catch (Exception e) {
            throw new WindowsLensException(MODEL_RESPONSE_ERROR, e);
        }
    }

    private void addChatMessage(
            MessageRole role, String message, UUID sessionId
    ) {
        ChatSession session = store.find(sessionId).orElseThrow(
                () -> new WindowsLensException(SESSION_NOT_FOUND_ERROR)
        );

        ChatMessage chatMessage = ChatMessage.builder().role(role)
                .content(message).build();

        session.getMessages().add(chatMessage);
    }
}
