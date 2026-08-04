package io.jatinjindal.backend.constant;

public class BackendConstants {
    private BackendConstants() {}
    public static final String ERROR = "[WINDOWS LENS ERROR]: ";
    public static final String MODEL_RESPONSE_ERROR = ERROR + "Failed to get model response.";
    public static final String MODEL_NOT_FOUND_ERROR = ERROR + "Model not found.";
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
    public static final String USER_HOME = "user.home";
    public static final String MODEL_LIST_PATH = ".windows-lens/models.txt";
    public static final String SESSION_NOT_FOUND_ERROR = ERROR + "Session not found.";
    public static final int FOLLOWUP_LIMIT = 3;
    public static final String SESSION_MAX_PROMPTS_ERROR = ERROR + "Session has reached the limit for prompts.";
}
