package com.dongsan.domains.user.mapper;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.user.dto.response.GetBookmarksResponse;
import com.dongsan.domains.user.dto.response.GetBookmarksResponse.BookmarkInfo;
import java.util.List;
import java.util.stream.Collectors;

public class UserBookmarkMapper {

    /**
     * List Bookmark -> GetBookmarkResponse
     */
    public static GetBookmarksResponse toGetBookmarksResponse(List<Bookmark> bookmarkList) {
        return GetBookmarksResponse.builder()
                .bookmarks(toBookmarkInfo(bookmarkList))
                .build();
    }

    /**
     * List Bookmark -> List BookmarkInfo
     */
    public static List<BookmarkInfo> toBookmarkInfo(List<Bookmark> bookmarkList) {
        return bookmarkList.stream()
                .map(bookmark -> BookmarkInfo.builder()
                        .bookmarkId(bookmark.getId())
                        .title(bookmark.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
