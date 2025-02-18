package com.dongsan.api.domains.dev;

<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/domains/dev/DevUseCase.java
import com.dongsan.api.domains.auth.service.JwtService;
import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.file.service.S3FileService;
=======
import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.AuthErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.service.CookieService;
import com.dongsan.domains.auth.service.JwtService;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.dev.mapper.DevMapper;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.service.S3FileService;
>>>>>>> 496a334bff8928cf4a3a20bc45dce34b0046eae7:app-external-api/src/main/java/com/dongsan/domains/dev/usecase/DevUseCase.java
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DevUseCase {
    private final MemberReader memberReader;
    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final S3FileService s3FileService;

    @Transactional
<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/domains/dev/DevUseCase.java
<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/domains/dev/DevUseCase.java
    public GenerateTokenResponse generateToken(Long memberId){
        memberReader.readMember(memberId);
=======
    public void generateToken(Long memberId, HttpServletRequest httpServletRequest, HttpServletResponse response){
=======
    public void generateToken(Long memberId, HttpServletResponse response){
>>>>>>> 920be9371ff304630f249d16536e70a3e734d4d6:app-external-api/src/main/java/com/dongsan/domains/dev/usecase/DevUseCase.java
        memberQueryService.getMember(memberId);
>>>>>>> 496a334bff8928cf4a3a20bc45dce34b0046eae7:app-external-api/src/main/java/com/dongsan/domains/dev/usecase/DevUseCase.java
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        authService.saveRefreshToken(memberId, refreshToken);
    }

    @Transactional
    public GetMemberInfoResponse getMemberInfo(HttpServletRequest request){
        String accessToken = cookieService.getAccessTokenFromCookie(request);
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
