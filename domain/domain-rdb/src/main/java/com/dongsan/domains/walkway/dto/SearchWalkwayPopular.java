package com.dongsan.domains.walkway.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SearchWalkwayPopular(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        LocalDateTime createdAt,
        Integer lastLikes,
        int size
) {
}
