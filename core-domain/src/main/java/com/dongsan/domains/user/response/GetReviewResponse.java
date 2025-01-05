package com.dongsan.domains.user.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetReviewResponse(
        List<ReviewInfo> reviews
) {
    @Builder
    public record ReviewInfo(
            Long reviewId,
            Long walkwayId,
            String walkwayName,
            String date,
            Integer rating,
            String content
    ){ }
}
