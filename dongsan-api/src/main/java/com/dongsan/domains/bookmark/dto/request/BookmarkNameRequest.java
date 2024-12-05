package com.dongsan.domains.bookmark.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BookmarkNameRequest(
        @NotBlank(message = "북마크 제목을 입력해주세요.")
        String name
) {
}
