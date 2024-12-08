package com.dongsan.domains.bookmark.dto;

public record BookmarksWithMarkedWalkwayDTO(
        Long bookmarkId,
        Long memberId,
        String name,
        Long markedWalkwayId
) {
}
