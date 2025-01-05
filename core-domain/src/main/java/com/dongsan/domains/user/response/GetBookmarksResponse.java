package com.dongsan.domains.user.response;

import lombok.Builder;

import java.util.List;


@Builder
public record GetBookmarksResponse(
        List<BookmarkInfo> bookmarks
) {
    @Builder
    public record BookmarkInfo(
            Long bookmarkId,
            String title
    ) {
    }
}
