package com.dongsan.domains.user.dto.response;

import lombok.Builder;

@Builder
public record GetProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
}
