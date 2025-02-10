package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.mapper.LikedWalkwayMapper;
import com.dongsan.rdb.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.rdb.domains.walkway.repository.LikedWalkwayJpaRepository;
import com.dongsan.rdb.domains.walkway.repository.LikedWalkwayQueryDSLRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayJpaRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayQueryDSLRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkwayCoreJpaRepository implements WalkwayRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;


    @Override
    public Walkway saveWalkway(Walkway walkway) {
        MemberEntity memberEntity = memberJpaRepository.findById(walkway.author().authorId())
                .orElseThrow(() -> new CoreException(CoreErrorCode.MEMBER_NOT_FOUND));

        WalkwayEntity walkwayEntity = WalkwayMapper.toWalkwayEntity(walkway, memberEntity);
        walkwayJpaRepository.save(walkwayEntity);

        return WalkwayMapper.toWalkway(walkwayEntity);
    }

    @Override
    public Walkway getWalkway(Long walkwayId) {
        WalkwayEntity walkwayEntity = walkwayJpaRepository.findById(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
        return WalkwayMapper.toWalkway(walkwayEntity);
    }

    @Override
    public void updateWalkway(Walkway updateWalkway) {
        WalkwayEntity walkwayEntity = walkwayJpaRepository.findById(updateWalkway.walkwayId())
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));

        walkwayEntity.updateWalkway(updateWalkway.name(), updateWalkway.memo(), updateWalkway.exposeLevel(), updateWalkway.hashtags());
        walkwayJpaRepository.save(walkwayEntity);
    }

    @Override
    public boolean existsWalkway(Long walkwayId) {
        return walkwayJpaRepository.existsById(walkwayId);
    }

    @Override
    public boolean existsWalkway(Long walkwayId, Long memberId) {
        return walkwayJpaRepository.existsByIdAndMemberId(walkwayId, memberId);
    }

    @Override
    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return likedWalkwayJpaRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public List<Walkway> searchWalkwaysLiked(Long userId, Long walkwayId, Double longitude, Double latitude, Double distance, int size) {
        WalkwayEntity lastWalkwayEntity = walkwayJpaRepository.findById(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysLiked(userId, lastWalkwayEntity, longitude, latitude, distance, size);
        return walkwayEntities.stream()
                .map(WalkwayMapper::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> searchWalkwaysRating(Long userId, Long walkwayId, Double longitude, Double latitude, Double distance, int size) {
        WalkwayEntity lastWalkwayEntity = walkwayJpaRepository.findById(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysRating(userId, lastWalkwayEntity, longitude, latitude, distance, size);
        return walkwayEntities.stream()
                .map(WalkwayMapper::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayMapper::toWalkway)
                .collect(Collectors.toList());
    }

    @Override
    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayMapper::toWalkway)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    @Override
    public LikedWalkway saveLikedWalkway(LikedWalkway likedWalkway) {
        MemberEntity memberEntity = memberJpaRepository.findById(likedWalkway.memberId())
                .orElseThrow(() -> new CoreException(CoreErrorCode.MEMBER_NOT_FOUND));

        WalkwayEntity lastWalkwayEntity = walkwayJpaRepository.findById(likedWalkway.walkwayId())
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));

        lastWalkwayEntity.increaseLikeCount();
        walkwayJpaRepository.save(lastWalkwayEntity);

        LikedWalkwayEntity likedWalkwayEntity = likedWalkwayJpaRepository.save(LikedWalkwayMapper.toLikedWalkwayEntity(memberEntity, lastWalkwayEntity));
        return LikedWalkwayMapper.toLikedWalkway(likedWalkwayEntity);
    }

    @Override
    public void deleteLikedWalkway(LikedWalkway likedWalkway) {
        WalkwayEntity lastWalkwayEntity = walkwayJpaRepository.findById(likedWalkway.walkwayId())
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));

        lastWalkwayEntity.decreaseLikeCount();
        walkwayJpaRepository.save(lastWalkwayEntity);

        likedWalkwayJpaRepository.deleteByMemberIdAndWalkwayId(likedWalkway.memberId(), likedWalkway.walkwayId());
    }

    @Override
    public LikedWalkway getLikedWalkway(Long memberId, Long walkwayId) {
        LikedWalkwayEntity likedWalkwayEntity = likedWalkwayJpaRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.LIKED_WALKWAY_NOT_FOUND));
        return LikedWalkwayMapper.toLikedWalkway(likedWalkwayEntity);
    }
}
