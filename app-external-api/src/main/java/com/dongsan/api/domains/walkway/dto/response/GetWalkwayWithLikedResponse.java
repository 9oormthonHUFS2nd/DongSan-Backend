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
        List<WalkwayCoordinate> course,
        Boolean marked,
        Integer likeCount
) {
    public GetWalkwayWithLikedResponse(Walkway walkway, boolean isMarked) {
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
<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/domains/walkway/dto/response/GetWalkwayWithLikedResponse.java
                LineStringMapper.toList(walkway.getCourse())
=======
                toList(walkway.getCourse()),
                isMarked,
                walkway.getLikeCount()
>>>>>>> 496a334bff8928cf4a3a20bc45dce34b0046eae7:core-domain/src/main/java/com/dongsan/domains/walkway/dto/response/GetWalkwayWithLikedResponse.java
        );
    }
}
