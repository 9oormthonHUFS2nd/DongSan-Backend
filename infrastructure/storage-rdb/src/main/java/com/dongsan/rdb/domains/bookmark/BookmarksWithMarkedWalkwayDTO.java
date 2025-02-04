package com.dongsan.rdb.domains.bookmark;

public record BookmarksWithMarkedWalkwayDTO(
        Long bookmarkId,
        Long memberId,
        String name,
        Long markedWalkwayId
) {
}
