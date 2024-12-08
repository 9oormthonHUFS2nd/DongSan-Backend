package com.dongsan.domains.bookmark.mapper;

import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse.BookmarkWithMarkedWalkway;
import java.util.List;

public class BookmarksWithMarkedWalkwayMapper {
    private BookmarksWithMarkedWalkwayMapper() {}

    public static BookmarksWithMarkedWalkwayResponse toBookmarksWithMarkedWalkwayResponse(List<BookmarksWithMarkedWalkwayDTO> bookmarks) {
        return BookmarksWithMarkedWalkwayResponse.builder()
                .bookmarks(bookmarks.stream()
                        .map(BookmarksWithMarkedWalkwayMapper::toBookmarkWithMarkedWalkway)
                        .toList())
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
