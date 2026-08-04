package io.jatinjindal.backend.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import java.util.function.Consumer;

import static io.jatinjindal.backend.constant.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletionServiceTest {

    @InjectMocks
    private CompletionService completionService;

    @Mock
    private OllamaService ollamaService;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    @BeforeEach
    void setOllamaChatMocks() {
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(any(Consumer.class))).thenReturn(requestSpec);

        when(requestSpec.options(any(OllamaChatOptions.Builder.class)))
                .thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
    }

    @Test
    @DisplayName("Success - suggestion is unsanitized & ollama is running")
    void getSuggestion_OllamaIsRunning_Success() {
        when(responseSpec.content()).thenReturn(TEST_UNSANITIZED_CONTENT);
        when(ollamaService.isOllamaRunning()).thenReturn(true);

        assertEquals(TEST_SUGGESTION, completionService
                .getSuggestion(TEST_REQUEST)
        ); verify(ollamaService, never()).startOllama();
    }

    @Test
    @DisplayName("Success - suggestion is unsanitized & ollama is NOT running")
    void getSuggestion_OllamaNotRunning_Success() {
        when(responseSpec.content()).thenReturn(TEST_UNSANITIZED_CONTENT);
        when(ollamaService.isOllamaRunning()).thenReturn(false);

        assertEquals(TEST_SUGGESTION, completionService
                .getSuggestion(TEST_REQUEST)
        ); verify(ollamaService, times(1)).startOllama();
    }

    @Test
    @DisplayName("Success - suggestion is sanitized")
    void getSuggestion_OllamaIsSanitized_Error() {
        when(responseSpec.content()).thenReturn(TEST_SANITIZED_CONTENT);
        when(ollamaService.isOllamaRunning()).thenReturn(false);

        assertEquals(TEST_SANITIZED_CONTENT, completionService
                .getSuggestion(TEST_REQUEST)
        ); verify(ollamaService, times(1)).startOllama();
    }

    @Test
    @DisplayName("Error - suggestion response is null")
    void getSuggestion_OllamaIsNull_Error() {
        when(responseSpec.content()).thenReturn(null);
        when(ollamaService.isOllamaRunning()).thenReturn(false);

        assertThrows(NullPointerException.class,
                () -> completionService.getSuggestion(TEST_REQUEST)
        ); verify(ollamaService, times(1)).startOllama();
    }

    @Test
    @DisplayName("Success - suggestion response is blank")
    void getSuggestion_OllamaIsBlank_Error() {
        when(responseSpec.content()).thenReturn("    ");
        when(ollamaService.isOllamaRunning()).thenReturn(false);

        assertEquals("", completionService.getSuggestion(TEST_REQUEST));
        verify(ollamaService, times(1)).startOllama();
    }
}
