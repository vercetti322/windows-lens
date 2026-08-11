package io.jatinjindal.backend.service;

import io.jatinjindal.backend.dto.common.MessageRole;
import io.jatinjindal.backend.dto.request.SessionRequest;
import io.jatinjindal.backend.dto.request.FollowupRequest;
import io.jatinjindal.backend.dto.response.SessionResponse;
import io.jatinjindal.backend.dto.response.FollowupResponse;
import io.jatinjindal.backend.exception.WindowsLensException;
import io.jatinjindal.backend.model.ChatMessage;
import io.jatinjindal.backend.model.ChatSession;
import io.jatinjindal.backend.store.ModelStore;
import io.jatinjindal.backend.store.SessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SessionStore sessionStore;
    private final ModelProvider modelProvider;
    private final ModelStore modelStore;

    public SessionResponse createSession(SessionRequest request) {
        if (!modelStore.containsModel(request.getModel())) {
            throw new WindowsLensException(MODEL_NOT_FOUND_ERROR);
        }

        var session = transformToSession(request); sessionStore.save(session);
        try {
            String response = modelProvider.chat(session.prompt(),
                    session.getModel(), session.getProvider()
            );
            addChatMessage(MessageRole.ASSISTANT, response, session.getId());

            return SessionResponse.builder().id(session.getId())
                    .response(response).build();
        } catch (Exception e) {
            throw new WindowsLensException(MODEL_RESPONSE_ERROR, e);
        }
    }

    private ChatSession transformToSession(SessionRequest request) {
        String model = request.getModel();
        String selectedText = request.getSelectedText();

        String userMessage = request.getUserMessage();
        var chatMessage = ChatMessage.builder().role(MessageRole.USER)
                .content(userMessage).timestamp(Instant.now()).build();

        return ChatSession.builder().id(UUID.randomUUID())
                .provider(request.getProvider()).model(model)
                .selectedText(selectedText).messages(
                        new ArrayList<>(List.of(chatMessage))).build();
    }

    public FollowupResponse sendFollowup(FollowupRequest request) {
        var session = sessionStore.find(request.getId()).orElseThrow(
                () -> new WindowsLensException(SESSION_NOT_FOUND_ERROR)
        );

        if (!modelStore.containsModel(request.getModel())) {
            throw new WindowsLensException(MODEL_NOT_FOUND_ERROR);
        }

        if (session.userMessageCount() > FOLLOWUP_LIMIT) {
            throw new WindowsLensException(SESSION_MAX_PROMPTS_ERROR);
        }

        addChatMessage(MessageRole.USER, request.getMessage(), session.getId());
        boolean sessionEnded = session.userMessageCount() > FOLLOWUP_LIMIT;

        try {
            String response = modelProvider.chat(session.prompt(),
                    session.getModel(), session.getProvider()
            );

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
        ChatSession session = sessionStore.find(sessionId).orElseThrow(
                () -> new WindowsLensException(SESSION_NOT_FOUND_ERROR)
        );

        ChatMessage chatMessage = ChatMessage.builder().role(role)
                .content(message).timestamp(Instant.now()).build();

        session.getMessages().add(chatMessage);
    }
}
