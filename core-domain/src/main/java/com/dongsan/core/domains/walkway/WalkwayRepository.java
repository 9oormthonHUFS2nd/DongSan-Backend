package com.dongsan.core.domains.walkway;

import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.domain.Walkway;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayRepository {
    // 산책로
    Walkway saveWalkway(Walkway walkway);
    Walkway getWalkway(Long walkwayId);
    void updateWalkway(Walkway updateWalkway);
    boolean existsWalkway(Long walkwayId);
    boolean existsWalkway(Long walkwayId, Long memberId);
    List<Walkway> searchWalkwaysLiked(Long userId, Long walkwayId, Double longitude, Double latitude, Double distance, int size);
    List<Walkway> searchWalkwaysRating(Long userId, Long walkwayId, Double longitude, Double latitude, Double distance, int size);
    List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);
    List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);

    // 좋아요
    boolean existsLikedWalkway(Long memberId, Long walkwayId);
    Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds);
    LikedWalkway saveLikedWalkway(LikedWalkway likedWalkway);
    void deleteLikedWalkway(LikedWalkway likedWalkway);
    LikedWalkway getLikedWalkway(Long memberId, Long walkwayId);
}
