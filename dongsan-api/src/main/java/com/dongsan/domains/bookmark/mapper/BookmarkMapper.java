package com.dongsan.domains.bookmark.mapper;

import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;

public class BookmarkMapper {
    private BookmarkMapper(){}

    /**
     * Long -> BookmarkIdResponse
     */
    public static BookmarkIdResponse toBookmarkIdResponse(Long bookmarkId){
        return BookmarkIdResponse.builder()
                .bookmarkId(bookmarkId)
                .build();
    }
}
