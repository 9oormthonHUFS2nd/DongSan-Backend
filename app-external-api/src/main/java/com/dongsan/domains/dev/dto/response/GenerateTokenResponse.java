package com.dongsan.domains.dev.dto.response;

import lombok.Builder;

@Builder
public record GenerateTokenResponse(
        String accessToken,
        String refreshToken
){ }
