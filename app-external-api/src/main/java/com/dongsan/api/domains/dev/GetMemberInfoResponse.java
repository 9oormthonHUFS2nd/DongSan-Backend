package com.dongsan.api.domains.dev;

import lombok.Builder;

@Builder
public record GetMemberInfoResponse(
        Long memberId,
        String email
) {
}
