package com.dongsan.domains.user.usecase;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberService;
import com.dongsan.domains.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileUsecase {

    private final MemberService memberService;

    public UserProfileDto.UserProfileRes getUserProfile(Long userId) {

        // TODO: 에러 코드 적용
        Member member = memberService.readMember(userId).orElseThrow();

        return UserProfileDto.UserProfileRes.of(member);
    }
}
