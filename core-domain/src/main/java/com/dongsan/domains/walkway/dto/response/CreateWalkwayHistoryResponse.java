package com.dongsan.domains.walkway.dto.response;

public record CreateWalkwayHistoryResponse(
        Long walkwayHistoryId,
        Boolean canReview
) {
}
