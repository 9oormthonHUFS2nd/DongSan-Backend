package com.dongsan.domains.walkway.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateReviewRequest(
        @NotNull(message = "별점을 입력해주세요.")
        Byte rating,
        String content
) {
}
