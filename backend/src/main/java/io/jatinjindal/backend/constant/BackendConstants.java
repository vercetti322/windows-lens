package io.jatinjindal.backend.constant;

import java.net.URI;

public class BackendConstants {
    private BackendConstants() {}
    public static final String ERROR = "[WINDOWS LENS ERROR]: ";
    public static final String MODEL_RESPONSE_ERROR = ERROR + "Failed to get model response.";
    public static final String MODEL_NOT_FOUND_ERROR = ERROR + "Model not found.";
    public static final String SYSTEM_PROMPT = """
        You are Windows Lens, an AI assistant for explaining and discussing text selected by the user.
        The selected text is the primary context. Use the conversation history to understand follow-up
        questions and maintain continuity.

        Rules:
        - Answer the user's request directly and concisely.
        - Keep responses brief and focused. Include only information relevant to the user's request.
        - Prefer a few clear sentences or short bullet points over long explanations.
        - Explain the selected text clearly at the user's apparent level of understanding.
        - For code, explain what it does, why it works, and relevant issues when asked.
        - For follow-up questions, use the selected text and previous conversation as context.
        - Do not repeat the selected text unless necessary.
        - Do not assume facts that are not present in the selected text or conversation when they matter.
        - If the selected text is ambiguous or insufficient, state what is missing briefly rather than inventing details.
        - Use examples only when they materially improve the explanation.
        - Format code, commands, and technical output appropriately.
        """;
    public static final String SESSION_NOT_FOUND_ERROR = ERROR + "Session not found.";
    public static final int FOLLOWUP_LIMIT = 3;
    public static final String SESSION_MAX_PROMPTS_ERROR = ERROR + "Session has reached the limit for prompts.";
    public static final String SELECTED_TEXT_HEADING = "Selected Text:\n";
    public static final String CONVERSATION_HEADING = "\n\nConversation:\n";
    public static final String GEMINI_API_KEY = "windows-lens-gemini-api-key";
    public static final String GEMINI_KEY_NOT_FOUND = ERROR + "Gemini API key not found.";
    public static final String LOCALHOST = "http://localhost:";
    public static final String GEMINI_RESPONSE_ERROR = ERROR + "Gemini response is null.";
    public static final String OLLAMA_RESPONSE_ERROR = ERROR + "Ollama response is null.";
    public static final String GEMINI_MODELS_RESPONSE_ERROR = ERROR + "Failed to get list of Gemini models.";
    public static final String OLLAMA_MODELS_RESPONSE_ERROR = ERROR + "Failed to get list of Ollama models.";
    public static final String GEMINI = "gemini";
    public static final String OLLAMA = "ollama";
    public static final String TAGS_PATH = "/api/tags";
    public static final String PS = "ps";
    public static final String SERVE = "serve";
    public static final String OLLAMA_START_ERROR = ERROR + "Failed to start Ollama server.";
    public static final URI GOOGLE_GEMINI_MODELS_URI = URI.create(
            "https://generativelanguage.googleapis.com/v1beta/models?pageSize=10"
    );
    public static final String API_KEY_HEADER = "x-goog-api-key";
    public static final String MODELS = "models";
    public static final String NAME = "name";
    public static final String PROVIDER_NOT_FOUND = ERROR + "Provider not found.";
    public static final String API_KEY_UPSERTED = "API key successfully upserted.";
    public static final String OLLAMA_PORT_NOT_FOUND =  ERROR + "Ollama Port not found.";
    public static final String OLLAMA_PORT_ERROR = ERROR + "Invalid port for Ollama.";
    public static final String USER_HOME = "user.home";
    public static final String WINDOWS_LENS = ".windows-lens";
    public static final String OLLAMA_SETTINGS_FILE = "ollama-settings.txt";
    public static final String OLLAMA_PORT = "OLLAMA_PORT";
    public static final String OLLAMA_SETTINGS_ERROR = ERROR + "Settings file not found for Ollama.";
    public static final String SESSION_LOGS_FOLDER = "sessions";
    public static final String JSONL = ".jsonl";
    public static final String FRONTEND_PORT = "19000";
}
