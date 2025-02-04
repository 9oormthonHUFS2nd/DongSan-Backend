package com.dongsan.api.domains.bookmark;

import lombok.Builder;

@Builder
public record BookmarkIdResponse(
        Long bookmarkId
) {
}
