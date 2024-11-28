package com.dongsan.domains.walkway.mapper;

import com.dongsan.common.format.TimeFormat;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public static GetWalkwayReviewsResponse toGetWalkwayReviewsResponse(List<Review> reviews) {
        return GetWalkwayReviewsResponse.builder()
                .reviews(toGetWalkwayReviewsResponseReview(reviews))
                .build();
    }

    public static List<GetWalkwayReviewsResponse.review> toGetWalkwayReviewsResponseReview(List<Review> reviews) {
        return reviews.stream()
                .map(review -> GetWalkwayReviewsResponse.review.builder()
                        .date(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .period(TimeFormat.formatTimeString(review.getCreatedAt()))
                        .nickname(review.getMember().getNickname())
                        .reviewId(review.getId())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
