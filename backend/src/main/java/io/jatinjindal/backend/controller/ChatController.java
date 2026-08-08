package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.dto.request.CreateSessionRequest;
import io.jatinjindal.backend.dto.request.FollowupRequest;
import io.jatinjindal.backend.dto.response.CreateSessionResponse;
import io.jatinjindal.backend.dto.response.FollowupResponse;
import io.jatinjindal.backend.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateSessionResponse createSession(
            @Valid @RequestBody CreateSessionRequest sessionRequest
    ) {
        return chatService.createSession(sessionRequest);
    }

    @PostMapping(value = "/sessions/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public FollowupResponse sendFollowup(
            @Valid @RequestBody FollowupRequest followupRequest
    ) {
        return chatService.sendFollowup(followupRequest);
    }
}
