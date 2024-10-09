package com.dongsan.domains.user.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetReview(
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
