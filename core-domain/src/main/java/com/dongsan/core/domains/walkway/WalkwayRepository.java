package com.dongsan.core.domains.walkway;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayRepository {
    // 산책로
    Long saveWalkway(CreateWalkway createWalkway);
    Walkway getWalkway(Long walkwayId);
    void updateWalkway(UpdateWalkway updateWalkway);
    boolean existsWalkway(Long walkwayId);
    boolean existsWalkway(Long walkwayId, Long memberId);
    List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery);
    List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery);
    List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);
    List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);

    // 좋아요
    boolean existsLikedWalkway(Long memberId, Long walkwayId);
    Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds);
    Long saveLikedWalkway(Long memberId, Long walkwayId);
    void deleteLikedWalkway(Long memberId, Long walkwayId);
//    LikedWalkway getLikedWalkway(Long memberId, Long walkwayId);
}
