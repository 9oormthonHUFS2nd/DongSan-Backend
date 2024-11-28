package com.dongsan.domains.user.mapper;

import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse.GetWalkwayDetailInfo;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse.UserWalkwaySummaryInfo;
import com.dongsan.domains.walkway.entity.Walkway;

import java.util.List;
import java.util.stream.Collectors;

public class UserWalkwayMapper {
    private UserWalkwayMapper(){}
    /**
     * List<Walkway> -> GetUserWalkwaySummary
     */
    public static GetWalkwaySummaryResponse toGetUserWalkwaySummaryResponse(List<Walkway> walkways){
        return GetWalkwaySummaryResponse.builder()
                .walkways(toUserWalkwaySummaryInfos(walkways))
                .build();
    }

    /**
     * List<Walkway> -> List<UserWalkwaySummaryInfo>
     */
    private static List<UserWalkwaySummaryInfo> toUserWalkwaySummaryInfos(List<Walkway> walkways){
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

    /**
     * List<Walkway> -> GetWalkwayDetailResponse
     */
    public static GetWalkwayDetailResponse toGetWalkwayDetailResponse(List<Walkway> walkways){
        return GetWalkwayDetailResponse.builder()
                .walkways(toGetWalkwayDetailInfos(walkways))
                .build();
    }

    /**
     * List<Walkway> -> List<GetWalkwayDetailInfo>
     */
    private static List<GetWalkwayDetailInfo> toGetWalkwayDetailInfos(List<Walkway> walkways){
        return walkways.stream()
                .map(w -> GetWalkwayDetailInfo.builder()
                        .walkwayId(w.getId())
                        .name(w.getName())
                        .date(w.getCreatedAt().toLocalDate())
                        .distance(w.getDistance())
                        .hashtags(toHashTag(w))
                        .courseImageUrl(w.getCourseImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Walkway -> List<String>
     */
    private static List<String> toHashTag(Walkway w){
        return w.getHashtagWalkways().stream().map(h -> h.getHashtag().getName()).toList();
    }
}
