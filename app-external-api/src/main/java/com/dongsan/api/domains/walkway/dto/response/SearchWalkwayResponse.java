package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.api.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.core.domains.walkway.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public record SearchWalkwayResponse(
        List<WalkwayResponse> walkways,
        Boolean hasNext
) {
    public SearchWalkwayResponse(List<Walkway> walkways, Map<Long, Boolean> isLiked, Integer size) {
        this(
                walkways.stream()
                        .map(walkway -> new WalkwayResponse(walkway, isLiked.get(walkway.walkwayId())))
                        .toList(),
                walkways.size() == size
        );
    }

    public record WalkwayResponse(
            Long walkwayId,
            String name,
            Double distance,
            List<String> hashtags,
            Boolean isLike,
            Integer likeCount,
            Integer reviewCount,
            Double rating,
            String courseImageUrl,
            WalkwayCoordinate location,
            String registerDate
    ) {
        public WalkwayResponse(Walkway walkway, Boolean isLiked) {
            this(
                    walkway.walkwayId(),
                    walkway.name(),
                    walkway.courseInfo().distance(),
                    walkway.hashtags().stream()
                            .map(hashtag -> "#" + hashtag)
                            .toList(),
                    isLiked,
                    walkway.stat().likeCount(),
                    walkway.stat().reviewCount(),
                    walkway.stat().rating(),
                    walkway.courseInfo().courseImageUrl(),
                    new WalkwayCoordinate(walkway.courseInfo().startLocation().getY(), walkway.courseInfo().startLocation().getX()),
                    walkway.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            );
        }
    }
}
