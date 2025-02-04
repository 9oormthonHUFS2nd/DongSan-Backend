package com.dongsan.api.domains.walkway.dto.response;

import lombok.Builder;

@Builder
public record GetWalkwayRatingResponse(
        Double rating,
        Integer reviewCount,
        Long five,
        Long four,
        Long three,
        Long two,
        Long one
) {
}
