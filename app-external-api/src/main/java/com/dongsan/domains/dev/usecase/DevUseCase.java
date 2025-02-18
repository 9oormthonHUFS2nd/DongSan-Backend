package com.dongsan.domains.dev.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.service.CookieService;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.dev.mapper.DevMapper;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.service.S3FileService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@RequiredArgsConstructor
public class DevUseCase {
    private final MemberQueryService memberQueryService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final S3FileService s3FileService;

    @Transactional
    public void generateToken(Long memberId, HttpServletResponse response){
        memberQueryService.getMember(memberId);
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        authService.saveRefreshToken(memberId, refreshToken);
    }

    @Transactional
    public GetMemberInfoResponse getMemberInfo(String accessToken){
        if(jwtService.isAccessTokenExpired(accessToken)){
            throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        return DevMapper.toGetMemberInfoResponse(member);
    }

    @Transactional
    public String uploadImage(MultipartFile image) throws IOException {
        return s3FileService.saveFile(image);
    }
}
