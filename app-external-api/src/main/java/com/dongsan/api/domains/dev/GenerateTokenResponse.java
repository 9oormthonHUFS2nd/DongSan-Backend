package com.dongsan.api.domains.dev;

import lombok.Builder;

@Builder
public record GenerateTokenResponse(
        String accessToken,
        String refreshToken
){ }
