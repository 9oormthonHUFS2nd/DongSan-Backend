package com.dongsan.api.domains.review;

import com.dongsan.domains.review.entity.Review;
import java.util.List;
import lombok.Builder;

@Builder
public record GetReviewResponse(
        List<ReviewInfo> reviews,
        boolean hasNext
) {
    public static GetReviewResponse from(List<Review> reviews, boolean hasNext){
        return new GetReviewResponse(reviews.stream().map(ReviewInfo::new).toList(),
                hasNext);
    }

    @Builder
    public record ReviewInfo(
            Long reviewId,
            Long walkwayId,
            String walkwayName,
            String date,
            Integer rating,
            String content
    ){
        public ReviewInfo(Review review){
            this(
                    review.getId(),
                    review.getWalkway().getId(),
                    review.getWalkway().getName(),
                    review.getCreatedAt().toString(),
                    review.getRating().intValue(),
                    review.getContent()
            );
        }
    }
}
