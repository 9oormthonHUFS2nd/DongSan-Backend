package com.dongsan.domains.walkway.dto;

import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;

public record SearchWalkwayRating(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        Walkway walkway,
        int size
) {
}
