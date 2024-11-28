package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse;
import com.dongsan.domains.user.mapper.UserWalkwayMapper;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UserWalkwayUseCase {
    private final WalkwayQueryService walkwayQueryService;

    public GetWalkwaySummaryResponse getUserWalkwaySummary(Long memberId, Integer size, Long walkwayId) {
        List<Walkway> walkways = walkwayQueryService.getUserWalkWay(memberId, size, walkwayId);
        return UserWalkwayMapper.toGetUserWalkwaySummaryResponse(walkways);
    }

    public GetWalkwayDetailResponse getUserWalkwayDetail(Long memberId, Integer size, Long walkwayId) {
        List<Walkway> walkways = walkwayQueryService.getUserWalkWay(memberId, size, walkwayId);
        return UserWalkwayMapper.toGetWalkwayDetailResponse(walkways);
    }
}
