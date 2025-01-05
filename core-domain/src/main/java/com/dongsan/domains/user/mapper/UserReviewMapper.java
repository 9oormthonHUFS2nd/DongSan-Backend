package com.dongsan.domains.user.mapper;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.user.response.GetReviewResponse;
import com.dongsan.domains.user.response.GetReviewResponse.ReviewInfo;
import java.util.List;
import java.util.stream.Collectors;

public class UserReviewMapper {
    private UserReviewMapper(){}
    /**
     * List<Review> 타입을 dto.response.GetReview 타입으로 변환한다.
     */
    public static GetReviewResponse toGetReview(List<Review> reviews){
        return GetReviewResponse.builder()
                .reviews(toReviewInfo(reviews))
                .build();
    }

    /**
     * List<Review> 타입을 dto.response.GetReview.List<ReviewInfo> 타입으로 변환한다.
     */
    private static List<ReviewInfo> toReviewInfo(List<Review> reviews){
        return reviews.stream()
                .map(r -> ReviewInfo.builder()
                        .reviewId(r.getId())
                        .walkwayId(r.getWalkway().getId())
                        .walkwayName(r.getWalkway().getName())
                        .date(r.getCreatedAt().toString())
                        .rating(r.getRating().intValue())
                        .content(r.getContent())
                        .build())
                .collect(Collectors.toList());
    }

}
