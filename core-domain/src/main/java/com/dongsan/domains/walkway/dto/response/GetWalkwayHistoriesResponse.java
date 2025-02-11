package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.entity.WalkwayHistory;
import java.time.LocalDate;
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
            Long walkwayId,
            String name,
            LocalDate date,
            Double distance,
            String courseImageUrl,
            Integer time,
            String memo,
            Integer likeCount,
            Integer reviewCount,
            Double rating,
            List<String> hashtags,
            Long walkwayHistoryId,
            LocalDateTime walkwayHistoryDate,
            Integer walkwayHistoryTime,
            Double walkwayHistoryDistance
    ) {
        public CanReviewWalkwayHistory(WalkwayHistory walkwayHistory) {
            this(
                    walkwayHistory.getWalkway().getId(),
                    walkwayHistory.getWalkway().getName(),
                    walkwayHistory.getWalkway().getCreatedAt().toLocalDate(),
                    walkwayHistory.getWalkway().getDistance(),
                    walkwayHistory.getWalkway().getCourseImageUrl(),
                    walkwayHistory.getWalkway().getTime(),
                    walkwayHistory.getWalkway().getMemo(),
                    walkwayHistory.getWalkway().getLikeCount(),
                    walkwayHistory.getWalkway().getReviewCount(),
                    walkwayHistory.getWalkway().getRating(),
                    walkwayHistory.getWalkway().getHashtagWalkways().stream()
                            .map(h -> h.getHashtag().getName())
                            .toList(),
                    walkwayHistory.getId(),
                    walkwayHistory.getCreatedAt(),
                    walkwayHistory.getTime(),
                    walkwayHistory.getDistance()
            );
        }
    }
}
