package com.dongsan.domains.auth.service;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CookieService {

    @Value("${cookie.domain}")
    private String domain;

    @Value("${cookie.access-name}")
    private String accessTokenName;

    @Value("${cookie.refresh-name}")
    private String refreshTokenName;

    @Value("${cookie.at-max-age}")
    private int accessTokenMaxAge;

    @Value("${cookie.rt-max-age}")
    private int refreshTokenMaxAge;

    public Cookie createAccessTokenCookie(String token) {
        return createTokenCookie(accessTokenName, token, accessTokenMaxAge);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return createTokenCookie(refreshTokenName, token, refreshTokenMaxAge);
    }

    public Cookie deleteAccessTokenCookie(){
        return createTokenCookie(accessTokenName, "", 0);
    }

    public Cookie deleteRefreshTokenCookie(){
        return createTokenCookie(refreshTokenName, "", 0);
    }

    private Cookie createTokenCookie(String cookieName, String token, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(false);

        // localhost 쿠키 전송을 위해
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(true);

        return cookie;
    }

}
