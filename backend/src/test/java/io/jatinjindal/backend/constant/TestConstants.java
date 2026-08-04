package io.jatinjindal.backend.constant;

import okhttp3.Headers;

public class TestConstants {
    private TestConstants() {}
    public static final String TEST_BEFORE_CURSOR = "public void reverse(int[] nums) {\n";
    public static final String TEST_LANGUAGE = "java";
    public static final String TEST_MODEL = "qwen2.5-coder:1.5b";
    public static final Double TEST_TEMPERATURE = 0.1;
    public static final Integer TEST_MAX_TOKENS = 36;
    public static final String TEST_SUGGESTION = "    nums.reverse();\n}";
    public static final String COMPLETIONS_PATH = "/api/completions";
    public static final String TEST_ERROR_RESPONSE = """
            {
                "error": "%s",
                "details": %s
            }
            """;
    public static final String TEST_INVALID_MODEL = "qwen25-code:1.5b";
    public static final String MODEL_NAME_NOT_FOUND = "HTTP 404 - {\"error\":\"model 'qwen25-code:1.5b' not found\"}";
    public static final ExplanationRequest TEST_REQUEST = ExplanationRequest.builder()
            .beforeCursor(TEST_BEFORE_CURSOR).language(TEST_LANGUAGE)
            .model(TEST_MODEL).temperature(TEST_TEMPERATURE)
            .maxTokens(TEST_MAX_TOKENS).build();

    public static final String TEST_UNSANITIZED_CONTENT = """
            ```
            public void reverse(int[] nums) {
                nums.reverse();
            }
            ```
            """;
    public static final String TEST_SANITIZED_CONTENT = "nums.reverse();";
    public static final Headers OLLAMA_TAG_HEADERS = Headers.of("Content-Type", "application/json");
    public static final Integer TEST_OLLAMA_PORT = 9999;
    public static final String TEST_OLLAMA_BASE_URL = "http://localhost:" + TEST_OLLAMA_PORT;
    public static final String OLLAMA_BASE_URL = "ollamaBaseUrl";
}