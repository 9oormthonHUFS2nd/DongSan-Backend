package com.dongsan.api.domains.walkway.dto.response;

import lombok.Builder;

@Builder
public record CreateReviewResponse(
        Long reviewId
) {
}
