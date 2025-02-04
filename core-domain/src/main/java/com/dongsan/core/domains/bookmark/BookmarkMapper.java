package com.dongsan.core.domains.bookmark;

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
