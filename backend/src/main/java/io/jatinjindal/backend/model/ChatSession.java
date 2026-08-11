package io.jatinjindal.backend.model;

import io.jatinjindal.backend.dto.common.MessageRole;
import io.jatinjindal.backend.dto.common.Provider;
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
    private Provider provider;
    private List<ChatMessage> messages;

    public String prompt() {
        StringBuilder sb = new StringBuilder(); sb.append(SELECTED_TEXT_HEADING);
        sb.append(selectedText); sb.append(CONVERSATION_HEADING);

        messages.forEach(message -> sb.append(message.role())
                .append(":").append(message.content()).append("\n"));

        return sb.toString();
    }

    public int userMessageCount() {
        return (int) messages.stream().filter(
                m -> m.role() == MessageRole.USER
        ).count();
    }
}
