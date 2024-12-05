package com.dongsan.domains.bookmark.mapper;

import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse.BookmarkWithMarkedWalkway;
import com.dongsan.domains.bookmark.entity.Bookmark;
import java.util.List;
import java.util.stream.Collectors;

public class BookmarksWithMarkedWalkwayMapper {
    private BookmarksWithMarkedWalkwayMapper() {}

    public static BookmarksWithMarkedWalkwayResponse toBookmarksWithMarkedWalkwayResponse(List<Bookmark> bookmarks) {
        return BookmarksWithMarkedWalkwayResponse.builder()
                .bookmarks(bookmarks.stream()
                        .map(BookmarksWithMarkedWalkwayMapper::toBookmarkWithMarkedWalkway)
                        .collect(Collectors.toList()))
                .build();
    }

    public static BookmarkWithMarkedWalkway toBookmarkWithMarkedWalkway(Bookmark bookmark) {
        return BookmarkWithMarkedWalkway.builder()
                .bookmarkId(bookmark.getId())
                .name(bookmark.getName())
                .marked(!bookmark.getMarkedWalkways().isEmpty())
                .build();
    }
}
