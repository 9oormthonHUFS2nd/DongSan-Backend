package com.dongsan.api.domains.walkway.mapper;

import com.dongsan.core.domains.walkway.domain.LikedWalkway;

public class LikedWalkwayMapper {
    private LikedWalkwayMapper(){}

    public static LikedWalkway toLikedWalkway(Long memberId, Long walkwayId) {
        return LikedWalkway.builder()
                .memberId(memberId)
                .walkwayId(walkwayId)
                .build();
    }
}
