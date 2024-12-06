package com.dongsan.domains.bookmark.dto.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record WalkwayIdRequest(
        @NonNull
        Long walkwayId
) {
}
