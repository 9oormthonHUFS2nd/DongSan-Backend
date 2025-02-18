package com.dongsan.domains.dev.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.usecase.AuthUseCase;
import com.dongsan.domains.dev.dto.request.GenerateTokenRequest;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.dev.dto.response.GetTokenRemaining;
import com.dongsan.domains.dev.usecase.DevUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dev")
@Tag(name = "개발용 API", description = "Develop API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevController {
    private final DevUseCase devUseCase;
    private final AuthUseCase authUseCase;
    private final AuthService authService;

    @Operation(summary = "개발용 토큰 발급")
    @PostMapping("/token")
    public ResponseEntity<Void> generateToken(
            @RequestBody GenerateTokenRequest dto,
            HttpServletResponse httpServletResponse
    ){
        devUseCase.generateToken(dto.memberId(), httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "accessToken으로 member 정보 확인하기")
    @GetMapping("/members")
    public ResponseEntity<SuccessResponse<GetMemberInfoResponse>> getMemberInfo(
            HttpServletRequest request
    ){
        GetMemberInfoResponse response = devUseCase.getMemberInfo(request);
        return ResponseFactory.ok(response);
    }

    @Operation(summary = "memberId로 서버에 저장된 refreshToken 확인하기")
    @GetMapping("/token/refresh")
    public ResponseEntity<SuccessResponse<Map<String, String>>> getRefreshToken(
            @RequestParam Long memberId
    ){
        String refreshToken = authService.getRefreshToken(memberId);
        Map<String, String> response = new HashMap<>();
        response.put("refreshToken", refreshToken);
        return ResponseFactory.ok(response);
    }

    @Operation(summary = "access token, refresh token의 만료기간 확인")
    @PostMapping("/token/expired")
    public ResponseEntity<SuccessResponse<GetTokenRemaining>> checkTokenExpire(
            HttpServletRequest request
    ){
        GetTokenRemaining response = authUseCase.checkTokenExpire(request);
        return ResponseFactory.ok(response);
    }

    @Operation(summary = "이미지 s3 저장 확인하기")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> uploadImage(
            @RequestPart("file") MultipartFile image
    )throws IOException {
        String url = devUseCase.uploadImage(image);
        return ResponseFactory.ok(url);
    }

}
