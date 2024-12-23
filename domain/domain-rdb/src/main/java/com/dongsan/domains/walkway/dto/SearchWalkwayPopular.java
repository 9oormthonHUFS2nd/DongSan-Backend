package com.dongsan.domains.walkway.dto;

import java.util.List;

public record SearchWalkwayPopular(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        Long lastId,
        Integer lastLikes,
        int size
) {
}
