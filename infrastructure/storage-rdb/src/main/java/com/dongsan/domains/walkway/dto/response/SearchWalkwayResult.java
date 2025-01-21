package com.dongsan.domains.walkway.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.locationtech.jts.geom.Point;

public record SearchWalkwayResult(
        Long id,
        String name,
        Double distance,
        List<String> hashtags,
        Boolean isLike,
        Integer likeCount,
        Integer reviewCount,
        Double rating,
        String courseImageUrl,
        Point startLocation,
        LocalDateTime createdAt
) {
}
