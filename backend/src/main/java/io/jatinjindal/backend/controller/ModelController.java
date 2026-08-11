package io.jatinjindal.backend.controller;

import io.jatinjindal.backend.dto.common.Model;
import io.jatinjindal.backend.service.ModelProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static io.jatinjindal.backend.constant.BackendConstants.*;

@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelProvider modelProvider;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Model> fetchModels(
            @RequestParam(value = GEMINI) boolean gemini,
            @RequestParam(value = OLLAMA) boolean ollama
    ) {
        return modelProvider.fetchAvailableModels(gemini, ollama);
    }
}
