package com.dongsan.domains.bookmark.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record BookmarksWithMarkedWalkwayResponse(
    List<BookmarkWithMarkedWalkway> bookmarks,
    Boolean hasNext
) {
    @Builder
    public record BookmarkWithMarkedWalkway (
            Long bookmarkId,
            String name,
            Boolean marked
    ) {}
}
