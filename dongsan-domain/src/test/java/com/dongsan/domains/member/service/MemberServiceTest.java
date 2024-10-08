package com.dongsan.domains.member.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
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
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("사용자 정보 조회")
    void MemberService_readMember_ReturnMember() {

        // Given
        Long memberId = 1L;

        Member member = Member.builder().build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));

        // When
        Optional<Member> memberReturn = memberService.readMember(memberId);

        // Then
        Assertions.assertThat(memberReturn).isNotNull();
    }
}