package io.jatinjindal.backend.constant;

public class BackendConstants {
    private BackendConstants() {}
    public static final String BEFORE_CURSOR = "beforeCursor";
    public static final String LANGUAGE = "language";
    public static final String ERROR = "error";
    public static final String MODEL = "model";
    public static final String TEMPERATURE = "temperature";
    public static final String MAX_TOKENS = "maxTokens";
    public static final String SYSTEM_PROMPT = """
        You are an inline code completion engine.
        
        Your job is to generate the text that should be inserted at the cursor.
        
        Rules:
        - Return only the text to insert.
        - Do not repeat text before the cursor.
        - Do not repeat text after the cursor.
        - Do not explain.
        - Do not use Markdown.
        - Preserve the coding style, indentation, and formatting.
        - If no completion is appropriate, return an empty response.
        """;
    public static final String COMPLETION_PROMPT = """
        Complete the following code.

        Language: {language}

        <PRECEDING_CODE>
        {beforeCursor}
        </PRECEDING_CODE>

        <CURSOR>

        <FOLLOWING_CODE>
        {afterCursor}
        </FOLLOWING_CODE>

        Return only the code that belongs at <CURSOR>.
        """;
    // to be replaced later -- start
    public static final String OLLAMA_TAGS_URI = "/api/tags";
    public static final String OLLAMA_PATH = "\"C:\\Users\\HP\\AppData\\Local\\Programs\\Ollama\\ollama.exe\"";
    public static final String OLLAMA_SERVE = "serve";
    // to be replaced later -- end
    public static final Integer CONNECTION_TIMEOUT = 5000;
    public static final Integer READ_TIMEOUT = 2000;
    public static final String OLLAMA_FAILED_TO_START = ERROR + ": Failed to start the OLLAMA engine.";
    public static final String SELECTED_TEXT_NOT_BLANK = "Selected text cannot be blank";
    public static final String LANGUAGE_REQUIRED = "Language cannot be blank";
    public static final String MODEL_NOT_BLANK = "Model cannot be blank";
    public static final String TEMPERATURE_REQUIRED = "Temperature cannot be null";
    public static final String TEMPERATURE_RANGE = "Temperature must be between 0.0 and 1.0";
    public static final String MAX_TOKENS_REQUIRED = "Max tokens cannot be null";
    public static final String MAX_TOKENS_MIN = "Max tokens must be at least 8";
    public static final String MAX_TOKENS_MAX = "Max tokens must be at most 64";
}
