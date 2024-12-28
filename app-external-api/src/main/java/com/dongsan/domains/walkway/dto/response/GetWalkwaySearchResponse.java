package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.entity.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record GetWalkwaySearchResponse(
        List<WalkwayResponse> walkways,
        Long nextCursor,
        int size
) {
    public GetWalkwaySearchResponse(List<Walkway> walkways, Integer size) {
        this(
                walkways.stream()
                        .map(WalkwayResponse::new)
                        .collect(Collectors.toList()),
                walkways.size() == size ? walkways.get(walkways.size()-1).getId() : -1,
                walkways.size()
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
            List<Double> location,
            String registerDate
    ) {
        public WalkwayResponse(Walkway walkway) {
            this(
                    walkway.getId(),
                    walkway.getName(),
                    walkway.getDistance(),
                    walkway.getHashtagWalkways().stream()
                            .map(hashtagWalkway -> "#" + hashtagWalkway.getHashtag().getName())
                            .collect(Collectors.toList()),
                    !walkway.getLikedWalkways().isEmpty(),
                    walkway.getLikeCount(),
                    walkway.getReviewCount(),
                    walkway.getRating(),
                    walkway.getCourseImageUrl(),
                    List.of(walkway.getStartLocation().getX(), walkway.getStartLocation().getY()),
                    walkway.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            );
        }
    }
}
