package com.dongsan.domains.dev.usecase;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.api.domains.auth.service.JwtService;
import com.dongsan.api.domains.dev.DevUseCase;
import com.dongsan.api.domains.dev.GenerateTokenResponse;
import com.dongsan.api.domains.dev.GetMemberInfoResponse;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.member.MemberReader;
import fixture.MemberFixture;
import org.assertj.core.api.Assertions;
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
    MemberReader memberReader;
    @Mock
    JwtService jwtService;
    @Mock
    AuthService authService;


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
            doNothing().when(authService).saveRefreshToken(memberId, refreshToken);

            // when
            GenerateTokenResponse response = devUseCase.generateToken(memberId);

            // then
            Assertions.assertThat(response.accessToken()).isEqualTo(accessToken);
            Assertions.assertThat(response.refreshToken()).isEqualTo(refreshToken);
        }
    }

    @Nested
    @DisplayName("getMemberInfo 메소드는")
    class Describe_getMemberInfoEntity {
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
            Assertions.assertThat(response.email()).isEqualTo(member.getEmail());
            Assertions.assertThat(response.memberId()).isEqualTo(member.getId());
        }
    }

}