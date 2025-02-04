package com.dongsan.core.domains.member;

import com.dongsan.domains.member.entity.Member;

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
