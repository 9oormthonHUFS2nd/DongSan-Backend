package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.entity.WalkwayHistory;
import java.time.LocalDateTime;

public record GetWalkwayHistoriesResponse(
        Long walkwayHistoryId,
        LocalDateTime date,
        Integer time,
        Double distance
) {
    public GetWalkwayHistoriesResponse(WalkwayHistory walkwayHistory) {
        this(
                walkwayHistory.getId(),
                walkwayHistory.getCreatedAt(),
                walkwayHistory.getTime(),
                walkwayHistory.getDistance()
        );
    }
}
