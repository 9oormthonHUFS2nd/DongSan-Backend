package com.dongsan.domains.walkway.dto.response;

import static com.dongsan.domains.walkway.mapper.LineStringMapper.toList;

import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record GetWalkwayWithLikedResponse(
        String date,
        Integer time,
        Double distance,
        String name,
        String memo,
        Double rating,
        Boolean isLiked,
        Integer reviewCount,
        List<String> hashTags,
        ExposeLevel accessLevel,
        List<List<Double>> course
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
                        .collect(Collectors.toList()),
                walkway.getExposeLevel(),
                toList(walkway.getCourse())
        );
    }
}
