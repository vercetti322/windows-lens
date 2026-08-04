package io.jatinjindal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowupResponse {
    private String response;
    private boolean sessionEnded;
}
