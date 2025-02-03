package com.dongsan.domains.walkway.service;

import com.dongsan.common.error.code.WalkwayHistoryErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.repository.WalkwayHistoryQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalkwayHistoryQueryService {
    private final WalkwayHistoryRepository walkwayHistoryRepository;
    private final WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;

    public WalkwayHistory findTop1ByWalkwayAndMember(Long walkwayId, Long memberId) {
        return walkwayHistoryRepository.findTop1ByWalkwayIdAndMemberIdOrderByCreatedAtDesc(walkwayId, memberId)
                .orElseThrow(() -> new CustomException(WalkwayHistoryErrorCode.HISTORY_NOT_FOUND));
    }

    public List<WalkwayHistory> getCanReviewWalkwayHistories(Long walkwayId, Long memberId) {
        return walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId);
    }
}
