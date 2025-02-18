package com.dongsan.domains.auth.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.service.CookieService;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GetTokenRemaining;
import com.dongsan.domains.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public void logout(Long memberId, HttpServletResponse response) {
        cookieService.deleteAllTokenCookie(response);
        authService.deleteRefreshToken(memberId);
    }

    /**
     * access token 만료 시 refresh token 을 보내서, access token & refresh token 둘다 갱신 만약 refresh token 기간 만료됨 -> 다시 로그인 시도
     * 하라고 에러 반환 만약 refresh token 기간 만료 안됨 -> Id(Key) 확인 후 value 와 동일한 토큰 인지 확인 (갱신 전 토큰 일 수도) -> value 와 다른 토큰 : 다시 로그인
     * 시도 하라고 에러 반환 -> value 와 동일한 토큰 : 토큰 갱신
     *
     * @param request
     * @param response
     * @return 새로 갱신한 access token & refresh token
     */
    public void renewToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getRefreshTokenFromCookie(request);
        if (!jwtService.isRefreshTokenExpired(refreshToken)) {
            Member member = jwtService.getMemberFromRefreshToken(refreshToken);
            if (authService.isRefreshTokenNotReplaced(member.getId(), refreshToken)) {
                String newAccessToken = jwtService.createAccessToken(member.getId());
                String newRefreshToken = jwtService.createRefreshToken(member.getId());
                response.addCookie(cookieService.createAccessTokenCookie(newAccessToken));
                response.addCookie(cookieService.createRefreshTokenCookie(newRefreshToken));
                authService.saveRefreshToken(member.getId(), newRefreshToken);
            }
            else{
                cookieService.deleteAllTokenCookie(response);
                throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
            }
        }
        else{
            cookieService.deleteAllTokenCookie(response);
            throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
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
