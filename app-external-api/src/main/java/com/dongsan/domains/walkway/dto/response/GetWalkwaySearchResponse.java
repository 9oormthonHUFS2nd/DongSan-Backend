package com.dongsan.domains.walkway.dto.response;

import com.dongsan.domains.walkway.entity.Walkway;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;

@Builder
public record GetWalkwaySearchResponse(
        List<WalkwayResponse> walkways,
        Long nextCursor
) {
    public GetWalkwaySearchResponse(List<Walkway> walkways, List<Boolean> likedWalkways, Integer size) {
        this(
                IntStream.range(0, walkways.size())
                        .mapToObj(i -> new WalkwayResponse(walkways.get(i), likedWalkways.get(i)))
                        .toList(),
                walkways.size() == size ? walkways.get(walkways.size()-1).getId() : -1
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
        public WalkwayResponse(Walkway walkway, Boolean isLike) {
            this(
                    walkway.getId(),
                    walkway.getName(),
                    walkway.getDistance(),
                    walkway.getHashtagWalkways().stream()
                            .map(hashtagWalkway -> "#" + hashtagWalkway.getHashtag().getName())
                            .collect(Collectors.toList()),
                    isLike,
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
