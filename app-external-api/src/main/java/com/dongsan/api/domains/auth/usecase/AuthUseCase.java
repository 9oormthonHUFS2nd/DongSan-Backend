package com.dongsan.api.domains.auth.usecase;

import com.dongsan.api.domains.auth.service.CookieService;
import com.dongsan.api.domains.auth.service.JwtService;
import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.error.ApiException;
import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.domains.dev.dto.response.GetTokenRemaining;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Transactional
public class AuthUseCase {
    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public void logout(Long memberId) {
        authService.logout(memberId);
    }

    /**
     * access token 만료 시 refresh token 을 보내서, access token & refresh token 둘다 갱신 만약 refresh token 기간 만료됨 -> 다시 로그인 시도
     * 하라고 에러 반환 만약 refresh token 기간 만료 안됨 -> Id(Key) 확인 후 value 와 동일한 토큰 인지 확인 (갱신 전 토큰 일 수도) -> value 와 다른 토큰 : 다시 로그인
     * 시도 하라고 에러 반환 -> value 와 동일한 토큰 : 토큰 갱신
     *
     * @param refreshToken 기존에 발급 받은 refresh token
     * @param request
     * @param response
     * @return 새로 갱신한 access token & refresh token
     */
    public void renewToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        if(jwtService.isRefreshTokenExpired(refreshToken)){
            throw new ApiException(ApiErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberFromRefreshToken(refreshToken).getId();
        if(authService.isRefreshTokenNotReplaced(memberId, refreshToken)){
            // 토큰 갱신
            String newAccessToken = jwtService.createAccessToken(memberId);
            String newRefreshToken = jwtService.createRefreshToken(memberId);
            authService.saveRefreshToken(memberId, newRefreshToken);
            response.addCookie(cookieService.createAccessTokenCookie(newAccessToken, request));
            response.addCookie(cookieService.createRefreshTokenCookie(newRefreshToken, request));
        }
        throw new ApiException(ApiErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    /**
     * 토큰의 만료기간을 확인하는 로직
     * @param accessToken
     * @param refreshToken
     */
    public GetTokenRemaining checkTokenExpire(String accessToken, String refreshToken) {
        long accessTokenRemaining = jwtService.getAccessTokenRemainingTimeMillis(accessToken);
        long refreshTokenRemaining = jwtService.getRefreshTokenRemainingTimeMillis(refreshToken);
        if(refreshTokenRemaining > 0){
            Long memberId = jwtService.getMemberFromRefreshToken(refreshToken).getId();
            if(!authService.isRefreshTokenNotReplaced(memberId, refreshToken)){
                refreshTokenRemaining = 0;
            }
        }

        return new GetTokenRemaining(accessTokenRemaining, refreshTokenRemaining);
    }
}
