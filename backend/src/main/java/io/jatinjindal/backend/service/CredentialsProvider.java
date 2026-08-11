package io.jatinjindal.backend.service;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredCredential;
import io.jatinjindal.backend.dto.common.Provider;
import io.jatinjindal.backend.exception.WindowsLensException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Service
public class CredentialsProvider {

    private final SecretStore<StoredCredential> storage = StorageProvider
            .getCredentialStorage(true, StorageProvider.SecureOption.REQUIRED);

    public Optional<String> get(String key) {
        if (storage == null) { return Optional.empty(); }
        StoredCredential credential = storage.get(key);

        if (credential == null) { return Optional.empty(); }
        try {
            return Optional.of(new String(credential.getPassword()));
        } finally { credential.clear(); }
    }

    public void put(Provider provider, String value) {
        if (storage == null) { return; }

        String key = switch (provider) {
            case GEMINI -> GEMINI_API_KEY;
            default -> throw new WindowsLensException(
                    PROVIDER_NOT_FOUND
            );
        };

        value = new String(Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8
        );

        storage.add(key, new StoredCredential(
                key, value.toCharArray())
        );
    }
}
