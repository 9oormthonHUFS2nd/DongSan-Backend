package com.dongsan.domains.user.dto.response;

import com.dongsan.domains.walkway.entity.Walkway;
import java.time.LocalDate;
import java.util.List;

public record WalkwayResponse(
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
        List<String> hashtags
) {
    public WalkwayResponse(Walkway w) {
        this(
            w.getId(),
            w.getName(),
            w.getCreatedAt()
                    .toLocalDate(),
            w.getDistance(),
            w.getCourseImageUrl(),
            w.getTime(),
            w.getMemo(),
            w.getLikeCount(),
            w.getReviewCount(),
            w.getRating(),
            w.getHashtagWalkways().stream()
                    .map(h -> h.getHashtag().getName())
                    .toList()
        );
    }
}