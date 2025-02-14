package com.dongsan.core.domains.walkway;

import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import java.util.List;

public record UpdateWalkway(
        Long walkwayId,
        String name,
        String memo,
        ExposeLevel exposeLevel,
        List<String> hashtags
) {
}
