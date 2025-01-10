package com.dongsan.domains.walkway.dto.request;

import java.util.List;

public record CreateWalkwayCourseRequest(
        List<List<Double>> course
) {
}
