package com.dongsan.api.domains.review;

import java.util.Map;

public record GetWalkwayRatingResponse(
        Double rating,
        Integer reviewCount,
        Long five,
        Long four,
        Long three,
        Long two,
        Long one
) {
    public static GetWalkwayRatingResponse from(Map<Integer, Long> ratingCounts) {
        Double totalRating = ratingCounts.entrySet().stream()
                .mapToDouble(entry -> entry.getKey() * entry.getValue())
                .sum();

        Integer totalReviewCount = (int) ratingCounts.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        Double avgRating = 0.0;
        if (totalReviewCount > 0) {
            avgRating = Math.round(totalRating / totalReviewCount * 10.0) / 10.0;
        }

        Long fiveRate = ratingCounts.get(5) * 100 / totalReviewCount;
        Long fourRate = ratingCounts.get(4) * 100 / totalReviewCount;
        Long threeRate = ratingCounts.get(3) * 100 / totalReviewCount;
        Long twoRate = ratingCounts.get(2) * 100 / totalReviewCount;
        Long oneRate = ratingCounts.get(1) * 100 / totalReviewCount;

        return new GetWalkwayRatingResponse(avgRating, totalReviewCount, fiveRate, fourRate, threeRate, twoRate, oneRate);
    }
}
