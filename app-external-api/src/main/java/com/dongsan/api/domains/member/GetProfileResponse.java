package com.dongsan.api.domains.member;

public record GetProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
}
