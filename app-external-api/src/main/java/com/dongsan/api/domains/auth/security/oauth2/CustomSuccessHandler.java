package com.dongsan.api.domains.auth.security.oauth2;

import com.dongsan.core.domains.auth.TokenWriter;
import com.dongsan.api.domains.auth.CookieService;
import com.dongsan.api.domains.auth.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final TokenWriter authWriter;

    @Value("${frontend.dev-redirect-url}")
    private String devRedirectUrl;

    @Value("${frontend.prod-redirect-url}")
    private String prodRedirectUrl;

    public CustomSuccessHandler(JwtService jwtService, CookieService cookieService, TokenWriter authWriter) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.authWriter = authWriter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customOAuth2User.getMemberId();

        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        authWriter.saveRefreshToken(memberId, refreshToken);

        String redirectUrl;
        String referer = request.getHeader("Referer");
        if (referer.contains("front.dongsanwalk.site")) {
            redirectUrl = devRedirectUrl;
        } else {
            redirectUrl = prodRedirectUrl;
        }

        response.sendRedirect(redirectUrl);
    }
}
