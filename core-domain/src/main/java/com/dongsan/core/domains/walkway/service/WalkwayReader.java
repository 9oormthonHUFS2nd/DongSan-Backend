package com.dongsan.core.domains.walkway.service;


import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.core.domains.walkway.service.factory.SearchWalkwayFactory;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalkwayReader {

    @Autowired
    public WalkwayReader(WalkwayRepository walkwayRepository, SearchWalkwayFactory searchWalkwayFactory) {
        this.walkwayRepository = walkwayRepository;
        this.searchWalkwayFactory = searchWalkwayFactory;
    }

    private final WalkwayRepository walkwayRepository;
    private final SearchWalkwayFactory searchWalkwayFactory;

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.getWalkway(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
    }

    public List<Walkway> getUserWalkWay(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return walkwayRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    public boolean existsWalkway(Long walkwayId) {
        return walkwayRepository.existsWalkway(walkwayId);
    }

//    public List<Walkway> getBookmarkWalkway(Bookmark bookmark, Integer size, LocalDateTime lastCreatedAt, Long memberId) {
//        return markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmark.getId(), size, lastCreatedAt, memberId)
//                .stream().map(MarkedWalkway::getWalkway).toList();
//    }

    public List<Walkway> searchWalkway(SearchWalkwayQuery searchWalkwayQuery, WalkwaySort sort) {
        return searchWalkwayFactory.getService(sort).search(searchWalkwayQuery);
    }

    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.existsLikedWalkway(memberId, walkwayId);
    }

    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return walkwayRepository.existsLikedWalkways(memberId, walkwayIds);
    }

//    public LikedWalkway getLikedWalkway(Long memberId, Long walkwayId) {
//        return walkwayRepository.getLikedWalkway(memberId, walkwayId);
//    }

    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
    }

//    public boolean isMarkedWalkway(Long walkwayId, Long memberId) {
//        return markedWalkwayQueryDSLRepository.existsMarkedWalkwayByMemberAndWalkway(walkwayId, memberId);
//    }
}
