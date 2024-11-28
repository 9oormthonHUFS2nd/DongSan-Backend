package com.dongsan.domains.user.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetWalkwayDetailResponse(
        List<GetWalkwayDetailInfo> walkways
) {

    @Builder
    public record GetWalkwayDetailInfo(
            Long walkwayId,
            String name,
            LocalDate date,
            Double distance,
            List<String> hashtags,
            String courseImageUrl
    ){ }
}
