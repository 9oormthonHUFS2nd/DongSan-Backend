package com.dongsan.rdb.domains.walkway.mapper;

import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;

public class LikedWalkwayMapper {
    private LikedWalkwayMapper() {}
    public static LikedWalkwayEntity toLikedWalkwayEntity(MemberEntity memberEntity, WalkwayEntity walkwayEntity) {
        return LikedWalkwayEntity.builder()
                .memberEntity(memberEntity)
                .walkwayEntity(walkwayEntity)
                .build();
    }

    public static LikedWalkway toLikedWalkway(LikedWalkwayEntity likedWalkwayEntity) {
        return LikedWalkway.builder()
                .id(likedWalkwayEntity.getId())
                .walkwayId(likedWalkwayEntity.getWalkwayEntity().getId())
                .memberId(likedWalkwayEntity.getMemberEntity().getId())
                .createdAt(likedWalkwayEntity.getCreatedAt())
                .build();
    }
}
