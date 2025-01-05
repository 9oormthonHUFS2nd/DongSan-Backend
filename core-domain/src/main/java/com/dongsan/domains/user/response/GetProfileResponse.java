package com.dongsan.domains.user.response;

import lombok.Builder;

@Builder
public record GetProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
}
