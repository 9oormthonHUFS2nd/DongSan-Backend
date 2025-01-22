package com.dongsan.domains.bookmark.dto.response;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GetBookmarkDetailResponse(
        String name,
        List<WalkwayInfo> walkways,
        boolean hasNext
) {
    public GetBookmarkDetailResponse(Bookmark bookmark, List<Walkway> walkways, boolean hasNext){
        this(
                bookmark.getName(),
                walkways.stream().map(WalkwayInfo::new).toList(),
                hasNext
        );
    }

    @Builder
    public record WalkwayInfo(
            Long walkwayId,
            String name,
            LocalDateTime date,
            Double distance,
            List<String> hashtags,
            String courseImageUrl
    ){
        WalkwayInfo(Walkway walkway){
            this(
                    walkway.getId(),
                    walkway.getName(),
                    walkway.getCreatedAt(),
                    walkway.getDistance(),
                    walkway.getHashtagWalkways().stream().map(h->h.getHashtag().getName()).toList(),
                    walkway.getCourseImageUrl()
            );
        }
    }
}
