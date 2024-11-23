package com.dongsan.domains.dev.dto.request;

import lombok.Builder;

@Builder
public record GenerateTokenRequest(
        Long memberId
) { }
