package com.dongsan.api.domains.bookmark;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WalkwayIdRequest(
        @NotNull
        Long walkwayId
) {
}
