package com.dongsan.rdb.domains.walkway.dto;

import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import java.util.List;

public record SearchWalkwayPopular(
        Long userId,
        Double longitude,
        Double latitude,
        int distance,
        List<String> hashtags,
        WalkwayEntity walkwayEntity,
        int size
) {
}
