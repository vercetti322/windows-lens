package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.service.CredentialsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static io.jatinjindal.backend.constant.BackendConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/credential")
public class CredentialController {

    private final CredentialsProvider credentialProvider;

    @PostMapping(value = "/key", produces = MediaType.APPLICATION_JSON_VALUE)
    public String upsertApiKey(
            @RequestParam(value = "provider") Provider provider,
            @RequestParam(value = "value") String value
    ) {
        credentialProvider.put(provider, value); return API_KEY_UPSERTED;
    }
}
