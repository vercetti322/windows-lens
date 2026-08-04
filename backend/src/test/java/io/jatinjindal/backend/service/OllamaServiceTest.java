package io.jatinjindal.backend.service;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static io.jatinjindal.backend.constant.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class OllamaServiceTest {

    private OllamaService service;
    private MockWebServer mockWebServer;

    @BeforeEach
    void startMockOllama() throws IOException {
        service = new OllamaService();
        mockWebServer = new MockWebServer();

        mockWebServer.start(TEST_OLLAMA_PORT);
        ReflectionTestUtils.setField(service,
                OLLAMA_BASE_URL, TEST_OLLAMA_BASE_URL
        );
    }

    @AfterEach
    void stopMockOllama() {
        mockWebServer.close();
    }

    @Test
    @DisplayName("ollama is running - /api/tags returns 200")
    void isOllamaRunning_returnsTrue() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .code(200).headers(OLLAMA_TAG_HEADERS).build());

        assertTrue(service.isOllamaRunning());
    }

    @Test
    @DisplayName("ollama not running - /api/tags returns 500")
    void isOllamaRunning_InternalServerError_returnsFalse() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .code(500).headers(OLLAMA_TAG_HEADERS).build());

        assertFalse(service.isOllamaRunning());
    }

    @Test
    @DisplayName("ollama not running - /api/tags is down")
    void isOllamaRunning_ServerDown_returnsFalse() {
        mockWebServer.close();
        assertFalse(service.isOllamaRunning());
    }
}
