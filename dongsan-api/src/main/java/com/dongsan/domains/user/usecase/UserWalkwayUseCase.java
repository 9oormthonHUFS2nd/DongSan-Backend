package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.user.dto.response.GetUserWalkwaySummary;
import com.dongsan.domains.user.mapper.UserWalkwayMapper;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UserWalkwayUseCase {
    private final WalkwayQueryService walkwayQueryService;

    @Transactional
    public GetUserWalkwaySummary getUserWalkwaySummary(Long memberId, Integer limit, Long walkwayId) {
        List<Walkway> walkways = walkwayQueryService.getUserWalkWay(memberId, limit, walkwayId);
        return UserWalkwayMapper.toGetUserWalkwaySummary(walkways);
    }
}
