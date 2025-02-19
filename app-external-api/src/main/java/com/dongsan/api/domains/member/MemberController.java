package com.dongsan.api.domains.member;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "마이페이지")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 유저 프로필 조회
     */
    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/users/profile")
    public ApiResponse<GetProfileResponse> getProfile(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = memberService.getMember(customOAuth2User.getMemberId());
        return ApiResponse.success(new GetProfileResponse(member.profileImageUrl(), member.email(), member.nickname()));
    }
}
