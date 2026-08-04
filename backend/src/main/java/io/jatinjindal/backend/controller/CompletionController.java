package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.service.CompletionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompletionController {

    private final CompletionService completionService;

    @PostMapping(value = "/completions", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSuggestion(@Valid @RequestBody ExplanationRequest request) {
        return completionService.getSuggestion(request);
    }
}
