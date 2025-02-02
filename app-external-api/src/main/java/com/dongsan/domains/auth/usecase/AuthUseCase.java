package com.dongsan.domains.auth.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.dto.RenewToken;
import com.dongsan.domains.auth.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthService authService;
    private final JwtService jwtService;

    public void logout(Long memberId) {
        authService.logout(memberId);
    }

    /**
     * access token 만료 시 refresh token 을 보내서, access token & refresh token 둘다 갱신
     * 만약 refresh token 기간 만료됨 -> 다시 로그인 시도 하라고 에러 반환
     * 만약 refresh token 기간 만료 안됨 -> Id(Key) 확인 후 value 와 동일한 토큰 인지 확인 (갱신 전 토큰 일 수도)
     *  -> value 와 다른 토큰 : 다시 로그인 시도 하라고 에러 반환
     *  -> value 와 동일한 토큰 : 토큰 갱신
     *
     * @param refreshToken  기존에 발급 받은 refresh token
     * @return  새로 갱신한 access token & refresh token
     */
    public RenewToken renewToken(String refreshToken) {
        if(jwtService.isRefreshTokenExpired(refreshToken)){
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberFromRefreshToken(refreshToken).getId();
        if(authService.isRefreshTokenNotReplaced(memberId, refreshToken)){
            // 토큰 갱신
            String newAccessToken = jwtService.createAccessToken(memberId);
            String newRefreshToken = jwtService.createRefreshToken(memberId);
            authService.saveRefreshToken(memberId, newRefreshToken);
            return new RenewToken(newAccessToken, newRefreshToken);
        }
        throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
