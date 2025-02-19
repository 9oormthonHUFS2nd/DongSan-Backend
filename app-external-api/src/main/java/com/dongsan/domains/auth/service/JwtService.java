package com.dongsan.domains.auth.service;

import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.common.error.code.SystemErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.enums.TokenType;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final MemberRepository memberRepository;
    private final AuthService authService;
    @Value("${jwt.access.secret}")
    private String accessTokenSecret;
    @Value("${jwt.access.expires-in}")
    private long accessTokenExpiresIn;
    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;
    @Value("${jwt.refresh.expires-in}")
    private long refreshTokenExpiresIn;
    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;

    @PostConstruct
    public void initialize(){
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
    }

    public String createAccessToken(Long memberId){
        return createToken(memberId, accessTokenExpiresIn, accessTokenSecretKey);
    }

    public String createRefreshToken(Long memberId){
        return createToken(memberId, refreshTokenExpiresIn, refreshTokenSecretKey);
    }

    private String createToken(Long memberId, long expiresIn, SecretKey secretKey){
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ expiresIn))
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

    public Member getMemberFromAccessToken(String accessToken){
        return getMember(accessToken, accessTokenSecretKey);
    }

    public Member getMemberFromRefreshToken(String refreshToken){
        return getMember(refreshToken, refreshTokenSecretKey);
    }

    private Member getMember(String token, SecretKey secretKey){
        Long memberId = extractAll(token, secretKey)
                .get("memberId", Long.class);
        return memberRepository.findById(memberId).orElseThrow(() -> {
            log.error("[AUTH] token에서 해당 id를 가진 member를 찾을 수 없습니다. memberId : {}", memberId);
            return new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        });
    }

    public boolean isAccessTokenExpired(String accessToken){
        return isTokenExpired(accessToken, accessTokenSecretKey, TokenType.ACCESS);
    }

    public boolean isRefreshTokenExpired(String refreshToken){
        return isTokenExpired(refreshToken, refreshTokenSecretKey, TokenType.REFRESH);
    }

    private boolean isTokenExpired(String token, SecretKey secretKey, TokenType tokenType) {
        if(token == null) return true;
        long remainingTime = getRemainingTimeMillis(token, secretKey);
        return remainingTime == 0;
    }

    public long getRemainingTimeMillis(String token, SecretKey secretKey){
        try{
            Date expiration = extractAll(token, secretKey).getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();
            return Math.max(remainingTime, 0);
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.info("[AUTH_INFO] JWT 토큰이 만료: {}", e.getMessage());
                return 0;
            }
            if (e instanceof MalformedJwtException) {
                log.warn("[AUTH_WARNING] JWT 토큰 형식이 올바르지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_FORMAT);
            } else if (e instanceof SignatureException) {
                log.warn("[AUTH_WARNING] JWT 토큰의 서명이 일치하지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_SIGNATURE);
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("[AUTH_WARNING] JWT 토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
            } else {
                log.error("[AUTH_ERROR] JWT 토큰 만료 검사중 알 수 없는 오류 발생: {}", e.getMessage());
                throw new CustomException(SystemErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public long getAccessTokenRemainingTimeMillis(String token){
        return getRemainingTimeMillis(token, accessTokenSecretKey);
    }

    public long getRefreshTokenRemainingTimeMillis(String token){
        return getRemainingTimeMillis(token, refreshTokenSecretKey);
    }

    /**
     * JWT 토큰의 모든 클레임을 추출한다.
     *
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return JWT 토큰에서 추출된 클레임
     */
    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 헤더에서 Access Token 추출 (Bearer + token)
     */
    public String getAccessTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.info("[AUTH] 헤더에서 access token을 찾을 수 없다.");
        throw new CustomException(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }


}
