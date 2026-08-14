package io.jatinjindal.backend.transmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jatinjindal.backend.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import static io.jatinjindal.backend.constant.BackendConstants.*;

@Component
@RequiredArgsConstructor
public class SessionTransmitter {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void persist(UUID sessionId, ChatMessage chatMessage) {
        Path sessions = Path.of(System.getProperty(USER_HOME),
                WINDOWS_LENS, SESSION_LOGS_FOLDER
        ); Path file = sessions.resolve(sessionId + JSONL);

        try {
            Files.createDirectories(sessions);
            Files.writeString(file, mapper.writeValueAsString(chatMessage)
                    + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
