package com.dongsan.domains.user.dto;


import com.dongsan.domains.member.entity.Member;

public class UserProfileDto {

    public record UserProfileRes(
            String profileImageUrl,
            String email,
            String nickname
    ) {
        public static UserProfileRes of(Member member) {
            return new UserProfileRes(member.getProfileImageUrl(), member.getEmail(), member.getNickname());
        }
    }

}
