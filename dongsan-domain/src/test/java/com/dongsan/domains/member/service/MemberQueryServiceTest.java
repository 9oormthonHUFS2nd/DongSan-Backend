package com.dongsan.domains.member.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberQueryService Unit Test")
class MemberQueryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberQueryService memberQueryService;

    @Nested
    @DisplayName("readMember 메서드는")
    class Describe_readMember {
        @Test
        @DisplayName("사용자가 존재하면 Optional Member를 반환한다.")
        void it_returns_optional_member() {

            // Given
            Long memberId = 1L;

            Member member = createMemberWithId(memberId);

            when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));

            // When
            Optional<Member> memberReturn = memberQueryService.readMember(memberId);

            // Then
            Assertions.assertThat(memberReturn).isNotNull();
        }
    }


}