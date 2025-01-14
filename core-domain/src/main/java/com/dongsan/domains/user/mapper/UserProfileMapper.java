package com.dongsan.domains.user.mapper;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.response.GetProfileResponse;

public class UserProfileMapper {
    private UserProfileMapper(){}

    /**
     * Member -> GetProfileResponse
     */
    public static GetProfileResponse toGetProfileResponse(Member member) {
        return GetProfileResponse.builder()
                .profileImageUrl(member.getProfileImageUrl())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
