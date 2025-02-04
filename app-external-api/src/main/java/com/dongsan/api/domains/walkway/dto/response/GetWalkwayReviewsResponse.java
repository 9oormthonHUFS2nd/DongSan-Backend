package com.dongsan.api.domains.walkway.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetWalkwayReviewsResponse(
    List<WalkwayReview> reviews,
    Boolean hasNext
) {
    @Builder
    public record WalkwayReview(
            Long reviewId,
            String nickname,
            String date,
            String period,
            Integer rating,
            String content
    ) { }
}
