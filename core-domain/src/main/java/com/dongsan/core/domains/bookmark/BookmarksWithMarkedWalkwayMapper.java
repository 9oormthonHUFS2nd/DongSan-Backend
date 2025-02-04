package com.dongsan.core.domains.bookmark;

import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.core.domains.bookmark.BookmarksWithMarkedWalkwayResponse.BookmarkWithMarkedWalkway;
import java.util.List;

public class BookmarksWithMarkedWalkwayMapper {
    private BookmarksWithMarkedWalkwayMapper() {}

    public static BookmarksWithMarkedWalkwayResponse toBookmarksWithMarkedWalkwayResponse(List<BookmarksWithMarkedWalkwayDTO> bookmarks, Integer size) {
        return BookmarksWithMarkedWalkwayResponse.builder()
                .bookmarks(bookmarks.stream()
                        .map(BookmarksWithMarkedWalkwayMapper::toBookmarkWithMarkedWalkway)
                        .toList())
                .hasNext(bookmarks.size() == size)
                .build();
    }

    public static BookmarkWithMarkedWalkway toBookmarkWithMarkedWalkway(BookmarksWithMarkedWalkwayDTO bookmark) {
        return BookmarkWithMarkedWalkway.builder()
                .bookmarkId(bookmark.bookmarkId())
                .name(bookmark.name())
                .marked(bookmark.markedWalkwayId() != null)
                .build();
    }
}
