package com.dongsan.domains.bookmark.dto.response;

import lombok.Builder;

@Builder
public record BookmarkIdResponse(
        Long bookmarkId
) {
}
