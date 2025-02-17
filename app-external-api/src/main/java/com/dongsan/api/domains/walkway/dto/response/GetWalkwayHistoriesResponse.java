package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.core.domains.walkway.WalkwayHistory;
import java.time.LocalDateTime;
import java.util.List;

public record GetWalkwayHistoriesResponse(
        List<CanReviewWalkwayHistory> walkwayHistories
) {

    public static GetWalkwayHistoriesResponse from(List<WalkwayHistory> walkwayHistories) {
        return new GetWalkwayHistoriesResponse(
                walkwayHistories.stream()
                        .map(CanReviewWalkwayHistory::new)
                        .toList()
        );
    }

    public record CanReviewWalkwayHistory(
            Long walkwayHistoryId,
            LocalDateTime date,
            Integer time,
            Double distance
    ) {
        public CanReviewWalkwayHistory(WalkwayHistory walkwayHistory) {
            this(
                    walkwayHistory.id(),
                    walkwayHistory.createdAt(),
                    walkwayHistory.time(),
                    walkwayHistory.distance()
            );
        }
    }
}
