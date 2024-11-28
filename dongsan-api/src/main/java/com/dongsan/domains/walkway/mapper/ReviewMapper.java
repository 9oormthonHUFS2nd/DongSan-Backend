package com.dongsan.domains.walkway.mapper;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.entity.Walkway;

public class ReviewMapper {

    public static Review toReview(CreateReviewRequest createReviewRequest, Walkway walkway, Member member) {
        return Review.builder()
                .walkway(walkway)
                .member(member)
                .content(createReviewRequest.content())
                .rating(createReviewRequest.rating())
                .build();
    }

    public static CreateReviewResponse toCreateReviewResponse(Review review) {
        return CreateReviewResponse.builder()
                .reviewId(review.getId())
                .build();
    }
}
