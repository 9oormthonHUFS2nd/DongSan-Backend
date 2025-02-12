package com.dongsan.domains.auth.dto;

public record RenewToken(
        String accessToken,
        String refreshToken
) {
}
