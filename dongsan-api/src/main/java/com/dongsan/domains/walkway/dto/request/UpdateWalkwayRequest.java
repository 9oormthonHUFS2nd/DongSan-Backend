package com.dongsan.domains.walkway.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateWalkwayRequest(
        @NotNull
        String name,
        @NotNull
        String memo,
        List<String> hashtags,
        String exposeLevel
) {
}
