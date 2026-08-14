package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.dto.request.ProviderRequest;
import io.jatinjindal.backend.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService service;

    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public String validate(@Valid @RequestBody ProviderRequest request) {
        return service.validate(request);
    }
}
