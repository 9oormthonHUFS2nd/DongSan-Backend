package com.dongsan.domains.walkway.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetWalkwayReviewsResponse(
    List<review> reviews
) {
    @Builder
    public record review (
            Long reviewId,
            String nickname,
            String date,
            String period,
            Byte rating,
            String content
    ) { }
}
