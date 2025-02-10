package com.dongsan.core.domains.walkway.domain;

import lombok.Builder;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Builder
public record CourseInfo(
        Double distance,
        Integer time,
        Point startLocation,
        Point endLocation,
        LineString course,
        String courseImageUrl
) {
}
