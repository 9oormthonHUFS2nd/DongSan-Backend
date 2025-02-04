package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.api.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.core.domains.walkway.mapper.LineStringMapper;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record GetWalkwayWithLikedResponse(
        String date,
        Integer time,
        Double distance,
        String name,
        String memo,
        Double rating,
        Boolean isLiked,
        Integer reviewCount,
        List<String> hashtags,
        ExposeLevel accessLevel,
        List<WalkwayCoordinate> course
) {
    public GetWalkwayWithLikedResponse(Walkway walkway) {
        this (
                walkway.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                walkway.getTime(),
                walkway.getDistance(),
                walkway.getName(),
                walkway.getMemo(),
                walkway.getRating(),
                !walkway.getLikedWalkways().isEmpty(),
                walkway.getReviewCount(),
                walkway.getHashtagWalkways().stream()
                        .map(hashtagWalkway -> "#" + hashtagWalkway.getHashtag().getName())
                        .toList(),
                walkway.getExposeLevel(),
                LineStringMapper.toList(walkway.getCourse())
        );
    }
}
