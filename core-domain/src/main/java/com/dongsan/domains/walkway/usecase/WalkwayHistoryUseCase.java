package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayHistoryResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.dongsan.domains.walkway.mapper.WalkwayHistoryMapper;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayHistoryUseCase {
    private final WalkwayHistoryCommandService walkwayHistoryCommandService;
    private final WalkwayQueryService walkwayQueryService;
    private final MemberQueryService memberQueryService;
    private final WalkwayHistoryQueryService walkwayHistoryQueryService;

    @Transactional
    public CreateWalkwayHistoryResponse createWalkwayHistory(Long memberId, Long walkwayId, CreateWalkwayHistoryRequest createWalkwayHistoryRequest) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
        Member member = memberQueryService.getMember(memberId);

        WalkwayHistory walkwayHistory = WalkwayHistoryMapper.toWalkwayHistory(createWalkwayHistoryRequest, walkway, member);

        Long walkwayHistoryId = walkwayHistoryCommandService.createWalkwayHistory(walkwayHistory).getId();
        boolean canReview = walkway.getDistance() * 2/3 < walkwayHistory.getDistance();
        return new CreateWalkwayHistoryResponse(walkwayHistoryId, canReview);
    }

    public List<WalkwayHistory> getWalkwayHistories(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        if (walkway.getExposeLevel().equals(ExposeLevel.PRIVATE)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_PRIVATE);
        }

        return walkwayHistoryQueryService.getCanReviewWalkwayHistories(walkwayId, memberId);
    }

    public List<WalkwayHistory> getUserWalkwayHistories(Long memberId, Long lastId, int size) {
        LocalDateTime lastCreatedAt = null;

        if (lastId != null) {
            WalkwayHistory walkwayHistory = walkwayHistoryQueryService.getById(lastId);
            lastCreatedAt = walkwayHistory.getCreatedAt();
        }

        return walkwayHistoryQueryService.getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt);
    }
}
