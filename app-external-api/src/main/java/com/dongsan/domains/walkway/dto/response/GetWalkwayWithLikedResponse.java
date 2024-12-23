package com.dongsan.domains.walkway.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GetWalkwayWithLikedResponse(
        String date,
        String time,
        Double distance,
        String name,
        String memo,
        Double rating,
        Boolean isLiked,
        Integer reviewCount,
        List<String> hashTags,
        Boolean accessLevel,
        List<List<Double>> course
) {
}
