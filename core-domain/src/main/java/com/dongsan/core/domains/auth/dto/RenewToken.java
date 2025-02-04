package com.dongsan.core.domains.auth.dto;

public record RenewToken(
        String accessToken,
        String refreshToken
) {
}
