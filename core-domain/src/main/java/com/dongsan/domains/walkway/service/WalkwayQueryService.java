package com.dongsan.domains.walkway.service;

import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.Sort;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import com.dongsan.domains.walkway.service.factory.SearchWalkwayServiceFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkwayQueryService {

    private final WalkwayRepository walkwayRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    private final MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository;
    private final SearchWalkwayServiceFactory searchWalkwayServiceFactory;

    public Walkway getWalkwayWithHashtagAndLike(Long userId, Long walkwayId) {
        return walkwayQueryDSLRepository.getUserWalkwayWithHashtagAndLike(userId, walkwayId);
    }

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.findById(walkwayId)
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND));
    }

    public List<Walkway> getUserWalkWay(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    public boolean existsByWalkwayId(Long walkwayId) {
        return walkwayRepository.existsById(walkwayId);
    }

    public List<Walkway> getBookmarkWalkway(Bookmark bookmark, Integer size, LocalDateTime lastCreatedAt) {
        return markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmark.getId(), size, lastCreatedAt)
                .stream().map(MarkedWalkway::getWalkway).toList();
    }

    public void isOwnerOfWalkway(Long walkwayId, Long memberId){
        boolean result = walkwayRepository.existsByIdAndMemberId(walkwayId, memberId);
        if(!result){
            throw new CustomException(WalkwayErrorCode.NOT_WALKWAY_OWNER);
        }
    }

    public List<SearchWalkwayResult> searchWalkway(SearchWalkwayQuery searchWalkwayRequest, Sort sort) {
        return searchWalkwayServiceFactory.getService(sort).search(searchWalkwayRequest);
    }
}
