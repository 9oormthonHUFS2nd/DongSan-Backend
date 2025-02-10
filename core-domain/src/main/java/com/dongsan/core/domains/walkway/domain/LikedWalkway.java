package com.dongsan.core.domains.walkway.domain;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LikedWalkway(
        Long id,
        Long memberId,
        Long walkwayId,
        LocalDateTime createdAt
) {
}
