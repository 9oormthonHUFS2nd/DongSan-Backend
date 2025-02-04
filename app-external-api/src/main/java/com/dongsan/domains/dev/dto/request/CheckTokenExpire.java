package com.dongsan.domains.dev.dto.request;

public record CheckTokenExpire(
        String accessToken,
        String refreshToken
) {
}
