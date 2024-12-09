package com.dongsan.domains.bookmark.mapper;

import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.dto.response.GetBookmarkDetailResponse;
import com.dongsan.domains.bookmark.dto.response.GetBookmarkDetailResponse.WalkwayInfo;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;

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

    /**
     * Bookmark, List<Walkway> -> GetBookmarkDetailResponse
     */
    public static GetBookmarkDetailResponse toGetBookmarkDetailResponse(Bookmark bookmark, List<Walkway> walkways){
        return GetBookmarkDetailResponse.builder()
                .name(bookmark.getName())
                .walkways(toWalkwayInfoList(walkways))
                .build();
    }

    /**
     * List<Walkway> -> List<WalkwayInfo>
     */
    private static List<WalkwayInfo> toWalkwayInfoList(List<Walkway> walkways){
        return walkways.stream()
                .map(w -> WalkwayInfo.builder()
                        .walkwayId(w.getId())
                        .name(w.getName())
                        .date(w.getCreatedAt())
                        .distance(w.getDistance())
                        .hashtags(w.getHashtagWalkways().stream().map(h->h.getHashtag().getName()).toList())
                        .courseImageUrl(w.getCourseImageUrl())
                        .build())
                .toList();
    }
}
