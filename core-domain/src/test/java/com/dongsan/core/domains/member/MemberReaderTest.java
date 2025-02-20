//package com.dongsan.domains.user.service;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.member.repository.MemberRepository;
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.core.domains.member.MemberReader;
//import java.util.Optional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("MemberQueryService Unit Test")
//class MemberReaderTest {
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @InjectMocks
//    private MemberReader memberReader;
//
//    @Nested
//    @DisplayName("getMember 메서드는")
//    class Describe_getMember {
//        @Test
//        @DisplayName("사용자가 존재하면 Member를 반환한다.")
//        void it_returns_member() {
//            // Given
//            Long memberId = 1L;
//
//            Member member = createMemberWithId(memberId);
//
//            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//
//            // When
//            Member result = memberReader.getMember(memberId);
//
//            // Then
//            Assertions.assertThat(result).isEqualTo(member);
//        }
//
//        @Test
//        @DisplayName("사용자가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_exception() {
//            // Given
//            Long memberId = 1L;
//
//            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> memberReader.getMember(memberId))
//                    .isInstanceOf(CustomException.class);
//        }
//    }
//
//
//}