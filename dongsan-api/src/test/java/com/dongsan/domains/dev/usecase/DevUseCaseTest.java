package com.dongsan.domains.dev.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GenerateTokenResponse;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DevUseCaseTest Unit Test")
class DevUseCaseTest {
    @InjectMocks
    DevUseCase devUseCase;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    JwtService jwtService;

    @Nested
    @DisplayName("generateToken 메소드는")
    class Describe_generateToken{
        @Test
        @DisplayName("사용자가 존재하면 token을 DTO로 변환한다.")
        void it_returns_responseDTO(){
            // given
            Long memberId = 1L;
            String accessToken = "ac_t";
            String refreshToken = "rf_t";
            when(jwtService.createAccessToken(memberId)).thenReturn(accessToken);
            when(jwtService.createRefreshToken(memberId)).thenReturn(refreshToken);

            // when
            GenerateTokenResponse response = devUseCase.generateToken(memberId);

            // then
            assertThat(response.accessToken()).isEqualTo(accessToken);
            assertThat(response.refreshToken()).isEqualTo(refreshToken);
        }
    }

    @Nested
    @DisplayName("getMemberInfo 메소드는")
    class Describe_getMemberInfo{
        @Test
        @DisplayName("accessToken에 사용자가 존재하면 사용자 정보를 DTO로 변환한다.")
        void it_returns_responseDTO(){
            // given
            String accessToken = "ac_t";
            Member member = MemberFixture.createMemberWithId(1L);
            when(jwtService.getMemberFromAccessToken(accessToken)).thenReturn(member);

            // when
            GetMemberInfoResponse response = devUseCase.getMemberInfo(accessToken);

            // then
            assertThat(response.email()).isEqualTo(member.getEmail());
            assertThat(response.memberId()).isEqualTo(member.getId());
        }
    }

}