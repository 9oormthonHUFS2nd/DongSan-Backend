package com.dongsan.domains.walkway.dto;

import java.util.List;

public record SearchWalkwayRating(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        Long lastId,
        Double lastRating,
        int size
) {
}
