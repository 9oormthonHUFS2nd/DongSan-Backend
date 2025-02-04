package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.domains.image.entity.Image;

public record CreateWalkwayCourseImageRequest(
        Long courseImageId
) {
    public CreateWalkwayCourseImageRequest(Image image) {
        this(
                image.getId()
        );
    }
}
