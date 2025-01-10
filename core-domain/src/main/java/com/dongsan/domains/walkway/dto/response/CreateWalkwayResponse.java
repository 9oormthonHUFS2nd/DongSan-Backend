package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.entity.Walkway;
import lombok.Builder;

@Builder
public record CreateWalkwayResponse(
        Long walkwayId
) {
    public CreateWalkwayResponse(Walkway walkway) {
        this (
                walkway.getId()
        );
    }
}
