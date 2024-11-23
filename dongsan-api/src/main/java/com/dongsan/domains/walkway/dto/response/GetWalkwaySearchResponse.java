package com.dongsan.domains.walkway.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetWalkwaySearchResponse(
        List<WalkwayResponse> walkways,
        Long nextCursor
) {
    @Builder
    public record WalkwayResponse(
        String name,
        Double distance,
        List<String> hashTags,
        Boolean isLike,
        Integer likeCount,
        Integer reviewCount,
        Double rating
    ) {

    }
}
