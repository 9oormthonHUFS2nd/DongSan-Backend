package com.dongsan.core.domains.review;

import com.dongsan.core.support.format.TimeFormat;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.core.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.core.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.core.domains.walkway.dto.response.GetWalkwayRatingResponse;
import com.dongsan.core.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.core.domains.walkway.dto.response.GetWalkwayReviewsResponse.WalkwayReview;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public static GetWalkwayReviewsResponse toGetWalkwayReviewsResponse(List<Review> reviews, Integer size) {
        return GetWalkwayReviewsResponse.builder()
                .reviews(toGetWalkwayReviewsResponseReview(reviews))
                .hasNext(reviews.size() == size)
                .build();
    }

    public static List<WalkwayReview> toGetWalkwayReviewsResponseReview(List<Review> reviews) {
        return reviews.stream()
                .map(review -> WalkwayReview.builder()
                        .date(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .period(TimeFormat.formatTimeString(review.getCreatedAt()))
                        .nickname(review.getMember().getNickname())
                        .reviewId(review.getId())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .build()
                )
                .toList();
    }

    public static GetWalkwayRatingResponse toGetWalkwayRatingResponse(List<RatingCount> ratingCounts, Walkway walkway) {
        // 별점 1 ~ 5 의 비율을 저장하는 리스트
        List<Long> ratings = new ArrayList<>(List.of(0L, 0L, 0L, 0L, 0L));

        // count 총합
        long totalCount = ratingCounts.stream()
                .mapToLong(RatingCount::count)
                .sum();

        // 비율 계산 (총 리뷰가 0이 아닐 경우)
        ratingCounts.forEach(ratingCount -> ratings.set(
                ratingCount.rating() - 1,
                (ratingCount.count() * 100 / totalCount)
        ));

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
