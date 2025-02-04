package com.dongsan.api.domains.dev;

import lombok.Builder;

@Builder
public record GenerateTokenRequest(
        Long memberId
) { }
