package com.dongsan.domains.walkway.dto.response;

import lombok.Builder;

@Builder
public record CreateWalkwayResponse(
        Long walkwayId
) {
}
