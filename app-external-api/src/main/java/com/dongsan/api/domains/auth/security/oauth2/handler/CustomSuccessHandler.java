package com.dongsan.api.domains.auth.security.oauth2.handler;

import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.api.domains.auth.service.CookieService;
import com.dongsan.api.domains.auth.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final AuthService authService;

    @Value("${frontend.dev-redirect-url}")
    private String devRedirectUrl;

    @Value("${frontend.prod-redirect-url}")
    private String prodRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customOAuth2User.getMemberId();

        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);

        // refreshToken 저장
        authService.saveRefreshToken(memberId, refreshToken);

        response.addCookie(cookieService.createAccessTokenCookie(accessToken, request));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken, request));

        // 주소 분기
        String redirectUrl;
        String origin = request.getHeader("Origin");
        log.info("[cookie : origin] " + origin);
        if (origin != null && origin.contains("localhost")) {
            redirectUrl = devRedirectUrl;
        } else {
            redirectUrl = prodRedirectUrl;
        }
        response.sendRedirect(redirectUrl);
    }
}
