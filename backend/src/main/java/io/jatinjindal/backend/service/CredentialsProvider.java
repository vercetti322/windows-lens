package io.jatinjindal.backend.service;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.StorageProvider;
import com.microsoft.credentialstorage.model.StoredCredential;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
}
