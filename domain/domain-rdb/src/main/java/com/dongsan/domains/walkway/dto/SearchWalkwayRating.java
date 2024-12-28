package com.dongsan.domains.walkway.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SearchWalkwayRating(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        LocalDateTime createdAt,
        Double lastRating,
        int size
) {
}
