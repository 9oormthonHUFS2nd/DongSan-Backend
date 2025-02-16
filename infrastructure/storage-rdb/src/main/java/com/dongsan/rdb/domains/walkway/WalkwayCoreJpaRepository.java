package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.repository.LikedWalkwayJpaRepository;
import com.dongsan.rdb.domains.walkway.repository.LikedWalkwayQueryDSLRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayJpaRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayQueryDSLRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalkwayCoreJpaRepository implements WalkwayRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    @Autowired
    public WalkwayCoreJpaRepository(
            MemberJpaRepository memberJpaRepository,
            LikedWalkwayJpaRepository likedWalkwayJpaRepository,
            LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository,
            WalkwayJpaRepository walkwayJpaRepository,
            WalkwayQueryDSLRepository walkwayQueryDSLRepository
    ) {
        this.memberJpaRepository = memberJpaRepository;
        this.likedWalkwayJpaRepository = likedWalkwayJpaRepository;
        this.likedWalkwayQueryDSLRepository = likedWalkwayQueryDSLRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayQueryDSLRepository = walkwayQueryDSLRepository;
    }

    // 생성용 dto만들기
    @Override
    public Optional<Long> saveWalkway(CreateWalkway createWalkway) {
        return memberJpaRepository.findById(createWalkway.memberId())
                .map(memberEntity -> {
                    WalkwayEntity walkwayEntity = new WalkwayEntity(createWalkway, memberEntity);
                    return walkwayJpaRepository.save(walkwayEntity).getId();
                });
    }

    @Override
    public Optional<Walkway> getWalkway(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId)
                .map(WalkwayEntity::toWalkway);
    }

    @Override
    public Optional<Long> updateWalkway(UpdateWalkway updateWalkway) {
        return walkwayJpaRepository.findById(updateWalkway.walkwayId())
                .map(walkwayEntity -> {
                    walkwayEntity.updateWalkway(updateWalkway.name(), updateWalkway.memo(), updateWalkway.exposeLevel(), updateWalkway.hashtags());
                    return walkwayJpaRepository.save(walkwayEntity).getId();
                });
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
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .collect(Collectors.toList());
    }

    @Override
    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    @Override
    public Optional<Long> saveLikedWalkway(Long memberId, Long walkwayId) {
        Optional<MemberEntity> memberEntityOptional = memberJpaRepository.findById(memberId);
        Optional<WalkwayEntity> walkwayEntityOptional = walkwayJpaRepository.findById(walkwayId);
        if (memberEntityOptional.isEmpty() || walkwayEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        MemberEntity memberEntity = memberEntityOptional.get();
        WalkwayEntity walkwayEntity = walkwayEntityOptional.get();

        walkwayEntity.increaseLikeCount();
        walkwayJpaRepository.save(walkwayEntity);

        LikedWalkwayEntity likedWalkwayEntity = likedWalkwayJpaRepository.save(new LikedWalkwayEntity(memberEntity, walkwayEntity));
        return Optional.of(likedWalkwayEntity.getId());
    }

    @Override
    public Optional<Long> deleteLikedWalkway(Long memberId, Long walkwayId) {
        Optional<WalkwayEntity> walkwayEntityOptional = walkwayJpaRepository.findById(walkwayId);
        if (walkwayEntityOptional.isEmpty() || !memberJpaRepository.existsById(memberId)) {
            return Optional.empty();
        }

        WalkwayEntity lastWalkwayEntity = walkwayEntityOptional.get();
        lastWalkwayEntity.decreaseLikeCount();
        walkwayJpaRepository.save(lastWalkwayEntity);
        likedWalkwayJpaRepository.deleteByMemberIdAndWalkwayId(memberId, walkwayId);

        return Optional.of(walkwayId);
    }

//    @Override
//    public LikedWalkway getLikedWalkway(Long memberId, Long walkwayId) {
//        LikedWalkwayEntity likedWalkwayEntity = likedWalkwayJpaRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)
//                .orElseThrow(() -> new CoreException(CoreErrorCode.LIKED_WALKWAY_NOT_FOUND));
//        return LikedWalkwayMapper.toLikedWalkway(likedWalkwayEntity);
//    }
}
