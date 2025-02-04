package com.dongsan.api.domains.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Value("${cookie.max-age}")
    private int maxAge;

    @Value("${cookie.dev-domain}")
    private String devDomain;

    @Value("${cookie.prod-domain}")
    private String prodDomain;

    @Value("${cookie.access-name}")
    private String accessTokenName;

    @Value("${cookie.refresh-name}")
    private String refreshTokenName;

    public Cookie createAccessTokenCookie(String token, HttpServletRequest request) {
        return createTokenCookie(accessTokenName, token, request);
    }

    public Cookie createRefreshTokenCookie(String token, HttpServletRequest request) {
        return createTokenCookie(refreshTokenName, token, request);
    }

    // 로그인 직후 로컬스토리지로 이동시키기 때문에 만료시간을 짧게 설정
    private Cookie createTokenCookie(String cookieName, String token, HttpServletRequest request) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(false);

        // localhost 쿠키 전송을 위해
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(true);

        // 로컬호스트와 배포 주소 분기
        String requestUrl = request.getRequestURL().toString();
        if (requestUrl.contains("localhost")) {
            cookie.setDomain(devDomain); // 로컬호스트 도메인
        } else {
            cookie.setDomain(prodDomain); // 배포 도메인
        }
        return cookie;
    }
}
