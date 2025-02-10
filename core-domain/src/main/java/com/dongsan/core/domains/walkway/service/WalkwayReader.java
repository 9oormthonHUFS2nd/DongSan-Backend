package com.dongsan.core.domains.walkway.service;


import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.core.domains.walkway.service.factory.SearchWalkwayFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkwayReader {

    private final WalkwayRepository walkwayRepository;
    private final SearchWalkwayFactory searchWalkwayFactory;

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.getWalkway(walkwayId);
    }

    public List<Walkway> getUserWalkWay(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return walkwayRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    public boolean existsWalkway(Long walkwayId) {
        return walkwayRepository.existsWalkway(walkwayId);
    }

    public List<Walkway> getBookmarkWalkway(Bookmark bookmark, Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmark.getId(), size, lastCreatedAt, memberId)
                .stream().map(MarkedWalkway::getWalkway).toList();
    }

    public void isOwnerOfWalkway(Long walkwayId, Long memberId){
        boolean result = walkwayRepository.existsWalkway(walkwayId, memberId);
        if(!result){
            throw new CoreException(CoreErrorCode.NOT_WALKWAY_OWNER);
        }
    }

    public List<Walkway> searchWalkway(Long userId, Long lastId, Double latitude, Double longitude, Double distance, int size, WalkwaySort sort) {
        return searchWalkwayFactory.getService(sort).search(userId, lastId, longitude, latitude, distance, size);
    }

    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.existsLikedWalkway(memberId, walkwayId);
    }

    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return walkwayRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    public LikedWalkway getLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.getLikedWalkway(memberId, walkwayId);
    }

    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
    }
}
