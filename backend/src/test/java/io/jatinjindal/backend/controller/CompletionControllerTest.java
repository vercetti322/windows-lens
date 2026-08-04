package io.jatinjindal.backend.controller;

import static io.jatinjindal.backend.constant.TestConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static io.jatinjindal.backend.constant.BackendConstants.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.jatinjindal.backend.service.CompletionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(CompletionController.class)
class CompletionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompletionService completionService;

    @Test
    @DisplayName("200 Ok - Generate inline suggestion")
    void generateSuggestion_Success() throws Exception {
        ExplanationRequest request = buildRequest(
                TEST_BEFORE_CURSOR, TEST_LANGUAGE, TEST_MODEL,
                TEST_TEMPERATURE, TEST_MAX_TOKENS
        );

        when(completionService.getSuggestion(request)).thenReturn(TEST_SUGGESTION);
        mockMvc.perform(post(COMPLETIONS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(content().string(TEST_SUGGESTION));
    }

    @ParameterizedTest(name = "400 Bad Request - {0} is null")
    @MethodSource("nullFieldRequests")
    void generateSuggestion_NullFields_BadRequest(
            String field, ExplanationRequest request, String message
    ) throws Exception {
        String expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_REQUEST_BODY.getMessage(),
                objectMapper.writeValueAsString(List.of(message))
        );

        mockMvc.perform(post(COMPLETIONS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("400 Bad Request - Before Cursor is blank")
    void generateSuggestion_BlankBeforeCursor_BadRequest() throws Exception {
        ExplanationRequest request = buildRequest(
                "", TEST_LANGUAGE, TEST_MODEL,
                TEST_TEMPERATURE, TEST_MAX_TOKENS
        );

        String  expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_REQUEST_BODY.getMessage(),
                objectMapper.writeValueAsString(List.of(SELECTED_TEXT_NOT_BLANK))
        );

        mockMvc.perform(post(COMPLETIONS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("400 Bad Request - Model is blank")
    void generateSuggestion_BlankModel_BadRequest() throws Exception {
        ExplanationRequest request = buildRequest(
                TEST_BEFORE_CURSOR, TEST_LANGUAGE, "",
                TEST_TEMPERATURE, TEST_MAX_TOKENS
        );

        String  expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_REQUEST_BODY.getMessage(),
                objectMapper.writeValueAsString(List.of(MODEL_NOT_BLANK))
        );

        mockMvc.perform(post(COMPLETIONS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("400 Bad Request - Temperature is out of range")
    void generateSuggestion_TemperatureOutOfRange_BadRequest() throws Exception {
        ExplanationRequest request = buildRequest(
                TEST_BEFORE_CURSOR, TEST_LANGUAGE,
                TEST_MODEL, 1.5, TEST_MAX_TOKENS
        );

        String  expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_REQUEST_BODY.getMessage(),
                objectMapper.writeValueAsString(List.of(TEMPERATURE_RANGE))
        );

        mockMvc.perform(post(COMPLETIONS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("400 Bad Request - Max Tokens is out of range")
    void generateSuggestion_MaxTokensOutOfRange_BadRequest() throws Exception {
        ExplanationRequest request = buildRequest(
                TEST_BEFORE_CURSOR, TEST_LANGUAGE, TEST_MODEL,
                TEST_TEMPERATURE, 100
        );

        String  expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_REQUEST_BODY.getMessage(),
                objectMapper.writeValueAsString(List.of(MAX_TOKENS_MAX))
        );

        mockMvc.perform(post(COMPLETIONS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("400 Bad Request - Invalid Model Name")
    void generateSuggestion_InvalidModelName_BadRequest() throws Exception {
        ExplanationRequest request = buildRequest(
                BEFORE_CURSOR, TEST_LANGUAGE, TEST_INVALID_MODEL,
                TEST_TEMPERATURE, TEST_MAX_TOKENS
        );

        String expectedJson = TEST_ERROR_RESPONSE.formatted(
                ErrorType.INVALID_MODEL_DETAILS.getMessage(),
                objectMapper.writeValueAsString(List.of(MODEL_NAME_NOT_FOUND))
        );

        when(completionService.getSuggestion(request)).thenThrow(
                new NonTransientAiException(MODEL_NAME_NOT_FOUND)
        );

        mockMvc.perform(post(COMPLETIONS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, JsonCompareMode.LENIENT));
    }

    private static Stream<Arguments> nullFieldRequests() {
        return Stream.of(
                Arguments.of(BEFORE_CURSOR, buildRequest(
                        null, TEST_LANGUAGE, TEST_MODEL, TEST_TEMPERATURE,
                        TEST_MAX_TOKENS), SELECTED_TEXT_NOT_BLANK
                ),
                Arguments.of(LANGUAGE, buildRequest(
                        TEST_BEFORE_CURSOR, null, TEST_MODEL, TEST_TEMPERATURE,
                        TEST_MAX_TOKENS), LANGUAGE_REQUIRED
                ),
                Arguments.of(MODEL, buildRequest(
                        TEST_BEFORE_CURSOR, TEST_LANGUAGE, null, TEST_TEMPERATURE,
                        TEST_MAX_TOKENS), MODEL_NOT_BLANK
                ),
                Arguments.of(TEMPERATURE, buildRequest(
                        TEST_BEFORE_CURSOR, TEST_LANGUAGE, TEST_MODEL, null,
                        TEST_MAX_TOKENS), TEMPERATURE_REQUIRED
                ),
                Arguments.of(MAX_TOKENS, buildRequest(
                        TEST_BEFORE_CURSOR, TEST_LANGUAGE, TEST_MODEL,
                        TEST_TEMPERATURE, null), MAX_TOKENS_REQUIRED
                )
        );
    }

    private static ExplanationRequest buildRequest(
            String beforeCursor, String language,
            String model, Double temperature, Integer maxTokens
    ) {
        var requestBuilder = ExplanationRequest.builder();
        if (beforeCursor != null) requestBuilder.beforeCursor(beforeCursor);
        if (language != null) requestBuilder.language(language);

        if (model != null) requestBuilder.model(model);
        if (temperature != null) requestBuilder.temperature(temperature);

        if (maxTokens != null) requestBuilder.maxTokens(maxTokens);
        return requestBuilder.build();
    }
}
