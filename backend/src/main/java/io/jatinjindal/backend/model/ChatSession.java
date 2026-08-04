package io.jatinjindal.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import static io.jatinjindal.backend.constant.BackendConstants.*;

@Data
@Builder
public class ChatSession {
    private UUID id;
    private String selectedText;
    private String model;
    private List<ChatMessage> messages;

    public String prompt() {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECTED_TEXT_HEADING);

        sb.append(selectedText);
        sb.append(CONVERSATION_HEADING);

        messages.forEach(message -> sb.append(message.role())
                .append(":").append(message.content()).append("\n"));

        return sb.toString();
    }
}
