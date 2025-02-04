package com.dongsan.core.domains.walkway.mapper;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;

public class LikedWalkwayMapper {
    private LikedWalkwayMapper() {}

    public static LikedWalkway toLikedWalkway(Member member, Walkway walkway) {
        return LikedWalkway.builder()
                .member(member)
                .walkway(walkway)
                .build();
    }

}
