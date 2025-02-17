package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;

public record MarkedWalkway(
        Long walkwayId,
        String name,
        LocalDateTime includedAt,
        Double distance,
        List<String> hashtags,
        String courseImageUrl
) {
}
