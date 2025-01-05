package com.dongsan.domains.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WalkwayIdRequest(
        @NotNull
        Long walkwayId
) {
}
