package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.List;

public record BookmarksWithMarkedWalkwayResponse(
    List<BookmarkWithMarkedWalkway> bookmarks,
    Boolean hasNext
) {
    public BookmarksWithMarkedWalkwayResponse(CursorPagingResponse<BookmarkWithMarkedStatus> response){
        this(
                response.data().stream().map(BookmarkWithMarkedWalkway::new).toList(),
                response.hasNext()
        );
    }
    public record BookmarkWithMarkedWalkway (
            Long bookmarkId,
            String name,
            Boolean marked
    ) {
        public BookmarkWithMarkedWalkway(BookmarkWithMarkedStatus bookmark){
            this(
                    bookmark.bookmarkId(),
                    bookmark.title(),
                    bookmark.marked()
            );
        }

    }
}
