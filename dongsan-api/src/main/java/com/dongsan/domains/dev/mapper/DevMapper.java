package com.dongsan.domains.dev.mapper;

import com.dongsan.domains.dev.dto.response.GenerateTokenResponse;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.member.entity.Member;

public class DevMapper {
    private DevMapper(){}

    /**
     * String, String 타입을 GenerateTokenResponse 타입으로 변환한다.
     */
    public static GenerateTokenResponse toGenerateTokenResponse(
            String accessToken, String refreshToken){
        return GenerateTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Member 타입을 GetMemberInfoResponse 타입으로 변환한다.
     */
    public static GetMemberInfoResponse toGetMemberInfoResponse(
            Member member
    ){
        return GetMemberInfoResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }
}
