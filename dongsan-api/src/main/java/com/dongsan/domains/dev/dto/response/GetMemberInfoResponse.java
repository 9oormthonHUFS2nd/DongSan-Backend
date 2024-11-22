package com.dongsan.domains.dev.dto.response;

import lombok.Builder;

@Builder
public record GetMemberInfoResponse(
        Long memberId,
        String email
) {
}
