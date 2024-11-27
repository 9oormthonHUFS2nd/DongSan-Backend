package com.dongsan.domains.user.mapper;

import com.dongsan.domains.user.dto.response.GetUserWalkwaySummary;
import com.dongsan.domains.user.dto.response.GetUserWalkwaySummary.UserWalkwaySummaryInfo;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;
import java.util.stream.Collectors;

public class UserWalkwayMapper {
    /**
     * List<Walkway> -> GetUserWalkwaySummary
     */
    public static GetUserWalkwaySummary toGetUserWalkwaySummary(List<Walkway> walkways){
        return GetUserWalkwaySummary.builder()
                .walkways(toUserWalkwaySummaryInfo(walkways))
                .build();
    }

    /**
     * List<Walkway> -> List<UserWalkwaySummaryInfo>
     */
    private static List<UserWalkwaySummaryInfo> toUserWalkwaySummaryInfo(List<Walkway> walkways){
        return walkways.stream()
                .map(w -> UserWalkwaySummaryInfo.builder()
                        .walkwayId(w.getId())
                        .name(w.getName())
                        .date(w.getCreatedAt().toLocalDate())
                        .distance(w.getDistance())
                        .courseImageUrl(w.getCourseImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
