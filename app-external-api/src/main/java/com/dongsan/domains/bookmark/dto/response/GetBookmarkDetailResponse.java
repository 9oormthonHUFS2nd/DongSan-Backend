package com.dongsan.domains.bookmark.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GetBookmarkDetailResponse(
        String name,
        List<WalkwayInfo> walkways
) {
    @Builder
    public record WalkwayInfo(
            Long walkwayId,
            String name,
            LocalDateTime date,
            Double distance,
            List<String> hashtags,
            String courseImageUrl
    ){}
}
