package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.mapper.WalkwayHistoryMapper;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayHistoryUseCase {
    private final WalkwayHistoryCommandService walkwayHistoryCommandService;
    private final WalkwayQueryService walkwayQueryService;
    private final MemberQueryService memberQueryService;

    @Transactional
    public Long createWalkwayHistory(Long memberId, Long walkwayId, CreateWalkwayHistoryRequest createWalkwayHistoryRequest) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
        Member member = memberQueryService.getMember(memberId);

        WalkwayHistory walkwayHistory = WalkwayHistoryMapper.toWalkwayHistory(createWalkwayHistoryRequest, walkway, member);

        return walkwayHistoryCommandService.createWalkwayHistory(walkwayHistory).getId();
    }
}
