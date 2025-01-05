package com.dongsan.domains.dev.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GenerateTokenResponse;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.dev.mapper.DevMapper;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.service.S3FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@RequiredArgsConstructor
public class DevUseCase {
    private final MemberQueryService memberQueryService;
    private final JwtService jwtService;
    private final S3FileService s3FileService;

    @Transactional
    public GenerateTokenResponse generateToken(Long memberId){
        memberQueryService.getMember(memberId);
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
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
