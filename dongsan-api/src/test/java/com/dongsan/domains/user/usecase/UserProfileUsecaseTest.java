package com.dongsan.domains.user.usecase;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberService;
import com.dongsan.domains.user.dto.UserProfileDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileUsecaseTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private UserProfileUsecase userProfileUsecase;


    @Test
    @DisplayName("마이페이지 유저 프로필 조회")
    void UserProfileUsecase_getUserProfile_ReturnUserProfileRes() {
        // Given
        Long memberId = 1L;

        Member member = Member.builder()
                .profileImageUrl("Test Url")
                .nickname("Test Nickname")
                .email("test@gmail.com")
                .build();

        UserProfileDto.UserProfileRes userProfileRes = UserProfileDto.UserProfileRes.of(member);

        when(memberService.readMember(memberId)).thenReturn(Optional.ofNullable(member));

        // When
        UserProfileDto.UserProfileRes userProfileResReturn = userProfileUsecase.getUserProfile(memberId);

        // Then
        Assertions.assertThat(userProfileResReturn)
                .isNotNull()
                .isEqualTo(userProfileRes);
    }
}