package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.dto.WalkwayCoordinate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record SearchWalkwayResponse(
        List<Walkway> walkways,
        Boolean hasNext
) {
    public SearchWalkwayResponse(List<SearchWalkwayResult> searchWalkwayResults, Integer size) {
        this(
                searchWalkwayResults.stream()
                        .map(Walkway::new)
                        .toList(),
                searchWalkwayResults.size() == size
        );
    }

    public record Walkway(
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
        public Walkway(SearchWalkwayResult walkway) {
            this(
                    walkway.id(),
                    walkway.name(),
                    walkway.distance(),
                    walkway.hashtags().stream()
                            .map(hashtag -> "#" + hashtag)
                            .toList(),
                    walkway.isLike(),
                    walkway.likeCount(),
                    walkway.reviewCount(),
                    walkway.rating(),
                    walkway.courseImageUrl(),
                    new WalkwayCoordinate(walkway.startLocation().getY(), walkway.startLocation().getX()),
                    walkway.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            );
        }
    }
}
