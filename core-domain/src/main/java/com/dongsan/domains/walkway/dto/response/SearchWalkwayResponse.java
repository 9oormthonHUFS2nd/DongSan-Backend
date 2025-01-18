package com.dongsan.domains.walkway.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record SearchWalkwayResponse(
        List<Walkway> walkways,
        Long nextCursor
) {
    public SearchWalkwayResponse(List<SearchWalkwayResult> searchWalkwayResults, Integer size) {
        this(
                searchWalkwayResults.stream()
                        .map(Walkway::new)
                        .toList(),
                searchWalkwayResults.size() == size ? searchWalkwayResults.get(size-1).id() : -1
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
            List<Double> location,
            String registerDate
    ) {
        public Walkway(SearchWalkwayResult walkway) {
            this(
                    walkway.id(),
                    walkway.name(),
                    walkway.distance(),
                    walkway.hashtags().stream()
                            .map(hashtag -> "#" + hashtag)
                            .collect(Collectors.toList()),
                    walkway.isLike(),
                    walkway.likeCount(),
                    walkway.reviewCount(),
                    walkway.rating(),
                    walkway.courseImageUrl(),
                    List.of(walkway.startLocation().getX(), walkway.startLocation().getY()),
                    walkway.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            );
        }
    }
}
