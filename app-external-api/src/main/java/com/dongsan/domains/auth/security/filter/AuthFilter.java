package com.dongsan.domains.auth.security.filter;

import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.auth.service.CookieService;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.common.error.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final AuthService authService;

    /**
     * 필터 안 타는 것들 여기에 작성
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Set<String> excludeUrl = new HashSet<>(Set.of(
        ));

        String url = request.getRequestURI();
        return excludeUrl.stream().anyMatch(url::startsWith);
    }

    /**
     * access 만료 (쿠키에 없음) <br>
     *
     *   1. refresh 만료 0
     *     -> 로그인 하라는 에러 반환 <br>
     *
     *   2. refresh 만료 X
     *     -> 토큰 둘다 재발급 후 쿠키에 다시 세팅 후 로직 수행
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String accessToken = cookieService.getAccessTokenFromCookie(request);
            if(jwtService.isAccessTokenExpired(accessToken)){
                String refreshToken = cookieService.getRefreshTokenFromCookie(request);
                if (!jwtService.isRefreshTokenExpired(refreshToken)) {
                    Member member = jwtService.getMemberFromRefreshToken(refreshToken);
                    if (authService.isRefreshTokenNotReplaced(member.getId(), refreshToken)) {
                        String newAccessToken = jwtService.createAccessToken(member.getId());
                        String newRefreshToken = jwtService.createRefreshToken(member.getId());
                        response.addCookie(cookieService.createAccessTokenCookie(newAccessToken));
                        response.addCookie(cookieService.createRefreshTokenCookie(newRefreshToken));
                        authService.saveRefreshToken(member.getId(), newRefreshToken);
                        authenticateUser(member);
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
            else{
                authenticateUser(jwtService.getMemberFromAccessToken(accessToken));
            }
        } catch (CustomException ex){
            log.error("[AUTH] CustomException 발생 : {}", ex.getMessage());
            request.setAttribute("error", ex.getErrorCode());
        } catch (Exception e){
            log.error("[AUTH] auth filter 에서 문제 발생 : {}", e.getMessage());
            request.setAttribute("error", null);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(Member member) {
        log.info("[AUTH] auth filter 사용자 정보, email : {}", member.getEmail());
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
