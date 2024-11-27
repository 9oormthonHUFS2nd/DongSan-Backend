package com.dongsan.domains.user.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record GetUserWalkwaySummary(
        List<UserWalkwaySummaryInfo> walkways
) {
    @Builder
    public record UserWalkwaySummaryInfo(
            Long walkwayId,
            String name,
            LocalDate date,
            Double distance,
            String courseImageUrl
    ){}

}
