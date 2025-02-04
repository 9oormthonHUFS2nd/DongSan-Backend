package com.dongsan.api.domains.user;

import lombok.Builder;

@Builder
public record GetProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
}
