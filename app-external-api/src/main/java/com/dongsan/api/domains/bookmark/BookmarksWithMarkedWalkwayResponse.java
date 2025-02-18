package com.dongsan.api.domains.bookmark;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.List;
import java.util.Map;

public record BookmarksWithMarkedWalkwayResponse(
    List<BookmarkWithMarkedWalkway> bookmarks,
    Boolean hasNext
) {
    public BookmarksWithMarkedWalkwayResponse(CursorPagingResponse<Bookmark> response, Map<Long, Boolean> isMarked) {
        this(
                response.data().stream()
                        .map(bookmark -> new BookmarkWithMarkedWalkway(bookmark.bookmarkId(), bookmark.title(), isMarked.get(bookmark.bookmarkId())))
                        .toList(),
                response.hasNext()
        );
    }

    public record BookmarkWithMarkedWalkway (
            Long bookmarkId,
            String name,
            Boolean marked
    ) {}
}
