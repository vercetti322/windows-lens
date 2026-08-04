package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.dto.request.CreateSessionRequest;
import io.jatinjindal.backend.dto.response.CreateSessionResponse;
import io.jatinjindal.backend.dto.response.FollowupResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @PostMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateSessionResponse createSession(
            @Valid @RequestBody CreateSessionRequest sessionRequest
    ) {
        return CreateSessionResponse.builder().build();
    }

    @PostMapping(value = "/sessions/{sessionId}/messages",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FollowupResponse sendFollowup(
            @PathVariable UUID sessionId,
            @NotBlank(message = "Prompt cannot be blank") String prompt
    ) {
        return FollowupResponse.builder().build();
    }
}
