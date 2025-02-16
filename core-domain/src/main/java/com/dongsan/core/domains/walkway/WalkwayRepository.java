package com.dongsan.core.domains.walkway;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayRepository {
    // 산책로
    Optional<Long> saveWalkway(CreateWalkway createWalkway);
    Optional<Walkway> getWalkway(Long walkwayId);
    Optional<Long> updateWalkway(UpdateWalkway updateWalkway);
    boolean existsWalkway(Long walkwayId);
    boolean existsWalkway(Long walkwayId, Long memberId);
    List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery);
    List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery);
    List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);
    List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);

    // 좋아요
    boolean existsLikedWalkway(Long memberId, Long walkwayId);
    Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds);
    Optional<Long> saveLikedWalkway(Long memberId, Long walkwayId);
    Optional<Long> deleteLikedWalkway(Long memberId, Long walkwayId);
//    LikedWalkway getLikedWalkway(Long memberId, Long walkwayId);
}
