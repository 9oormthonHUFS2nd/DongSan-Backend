package com.dongsan.api.domains.walkway.dto.response;


import com.dongsan.core.domains.walkway.domain.Walkway;
import lombok.Builder;

@Builder
public record CreateWalkwayResponse(
        Long walkwayId
) {
    public CreateWalkwayResponse(Walkway walkway) {
        this (
                walkway.walkwayId()
        );
    }
}
