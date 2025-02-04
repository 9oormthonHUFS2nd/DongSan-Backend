package com.dongsan.api.domains.bookmark;

import lombok.Builder;

import java.util.List;


@Builder
public record GetBookmarksResponse(
        List<BookmarkInfo> bookmarks,
        Boolean hasNext
) {
    @Builder
    public record BookmarkInfo(
            Long bookmarkId,
            String title
    ) {
    }
}
