package com.dongsan.api.domains.dev;

import com.dongsan.api.domains.auth.service.JwtService;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.file.service.S3FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@RequiredArgsConstructor
public class DevUseCase {
    private final MemberReader memberReader;
    private final AuthService authService;
    private final JwtService jwtService;
    private final S3FileService s3FileService;

    @Transactional
    public GenerateTokenResponse generateToken(Long memberId){
        memberReader.getMember(memberId);
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        authService.saveRefreshToken(memberId, refreshToken);

        return DevMapper.toGenerateTokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public GetMemberInfoResponse getMemberInfo(String accessToken){
        jwtService.isAccessTokenExpired(accessToken);
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        return DevMapper.toGetMemberInfoResponse(member);
    }

    @Transactional
    public String uploadImage(MultipartFile image) throws IOException {
        return s3FileService.saveFile(image);
    }
}
