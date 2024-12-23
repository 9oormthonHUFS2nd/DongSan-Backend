package com.dongsan.domains.bookmark.dto.param;

public record GetBookmarkDetailParam(
        Long memberId,
        Long bookmarkId,
        Integer size,
        Long walkwayId
) {
}
