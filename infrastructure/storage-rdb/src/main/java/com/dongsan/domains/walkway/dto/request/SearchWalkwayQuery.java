package com.dongsan.domains.walkway.dto.request;

import com.dongsan.domains.walkway.entity.Walkway;

public record SearchWalkwayQuery(
        Long userId,
        Double longitude,
        Double latitude,
        Double distance,
        Walkway walkway,
        int size
) {
}
