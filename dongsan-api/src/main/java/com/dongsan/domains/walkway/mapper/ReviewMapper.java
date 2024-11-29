package com.dongsan.domains.walkway.mapper;

import com.dongsan.common.format.TimeFormat;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayRatingResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse.walkwayReview;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    private ReviewMapper () {
    }

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

    public static List<walkwayReview> toGetWalkwayReviewsResponseReview(List<Review> reviews) {
        return reviews.stream()
                .map(review -> walkwayReview.builder()
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

    public static GetWalkwayRatingResponse toGetWalkwayRatingResponse(List<RatingCount> ratingCounts, Walkway walkway) {
        List<Long> ratings = new ArrayList<>(List.of(0L, 0L, 0L, 0L, 0L));
        Long total = 0L;
        for(RatingCount ratingCount : ratingCounts) {
            ratings.set(ratingCount.rating()-1, ratingCount.count());
            total += ratingCount.count();
        }

        if (total != 0) {
            for (int i = 0; i < 5; i++) {
                ratings.set(i, (ratings.get(i) * 100)/total) ;
            }
        }

        return GetWalkwayRatingResponse.builder()
                .rating(Math.floor(walkway.getRating() * 10) / 10)
                .reviewCount(walkway.getReviewCount())
                .five(ratings.get(4))
                .four(ratings.get(3))
                .three(ratings.get(2))
                .two(ratings.get(1))
                .one(ratings.get(0))
                .build();
    }
}
