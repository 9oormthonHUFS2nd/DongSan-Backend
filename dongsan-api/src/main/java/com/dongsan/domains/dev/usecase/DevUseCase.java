package com.dongsan.domains.dev.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GenerateTokenResponse;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class DevUseCase {
    private final MemberQueryService memberQueryService;
    private final JwtService jwtService;

    @Transactional
    public GenerateTokenResponse generateToken(Long memberId){
        memberQueryService.getMember(memberId);
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        return GenerateTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public GetMemberInfoResponse getMemberInfo(String accessToken){
        jwtService.isAccessTokenExpired(accessToken);
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        return GetMemberInfoResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }

}
