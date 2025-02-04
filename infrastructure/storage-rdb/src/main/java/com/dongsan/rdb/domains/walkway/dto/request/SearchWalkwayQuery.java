package com.dongsan.rdb.domains.walkway.dto.request;

import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;

public record SearchWalkwayQuery(
        Long userId,
        Double longitude,
        Double latitude,
        Double distance,
        WalkwayEntity walkwayEntity,
        int size
) {
}
