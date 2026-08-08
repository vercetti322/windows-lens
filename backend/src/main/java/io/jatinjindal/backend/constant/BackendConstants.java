package io.jatinjindal.backend.constant;

public class BackendConstants {
    private BackendConstants() {}
    public static final String ERROR = "[WINDOWS LENS ERROR]: ";
    public static final String MODEL_RESPONSE_ERROR = ERROR + "Failed to get model response.";
    public static final String MODEL_NOT_FOUND_ERROR = ERROR + "Model not found.";
    public static final String SYSTEM_HEADER = "SYSTEM PROMPT\n";
    public static final String SYSTEM_PROMPT = """
            You are Windows Lens, an AI assistant for explaining and discussing text selected by the user.
            The selected text is the primary context. Use the conversation history to understand follow-up
            questions and maintain continuity.

            Rules:
            - Answer the user's request directly and concisely.
            - Explain the selected text clearly at the user's apparent level of understanding.
            - For code, explain what it does, why it works, and relevant issues when asked.
            - For follow-up questions, use the selected text and previous conversation as context.
            - Do not repeat the selected text unless necessary.
            - Do not assume facts that are not present in the selected text or conversation when they matter to the answer.
            - If the selected text is ambiguous or insufficient, say what is missing rather than inventing details.
            - Use examples when they make the explanation clearer.
            - Format code, commands, and technical output appropriately.
        """;
    public static final String USER_HOME = "user.home";
    public static final String MODEL_LIST_PATH = ".windows-lens/models.txt";
    public static final String SESSION_NOT_FOUND_ERROR = ERROR + "Session not found.";
    public static final int FOLLOWUP_LIMIT = 3;
    public static final String SESSION_MAX_PROMPTS_ERROR = ERROR + "Session has reached the limit for prompts.";
    public static final String SELECTED_TEXT_HEADING = "Selected Text:\n";
    public static final String CONVERSATION_HEADING = "\n\nConversation:\n";
}
