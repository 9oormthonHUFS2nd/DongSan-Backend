package com.dongsan.domains.walkway.mapper;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;

public class WalkwayHistoryMapper {

    public static WalkwayHistory toWalkwayHistory(CreateWalkwayHistoryRequest createWalkwayHistoryRequest, Walkway walkway, Member member) {
        return WalkwayHistory.builder()
                .member(member)
                .walkway(walkway)
                .time(createWalkwayHistoryRequest.time())
                .distance(createWalkwayHistoryRequest.distance())
                .build();
    }
}
