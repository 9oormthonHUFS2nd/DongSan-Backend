package com.dongsan.api.domains.auth.controller;

import com.dongsan.core.common.apiResponse.ResponseFactory;
import com.dongsan.core.common.apiResponse.SuccessResponse;
import com.dongsan.api.domains.auth.usecase.AuthUseCase;
import com.dongsan.core.domains.auth.dto.RefreshToken;
import com.dongsan.core.domains.auth.dto.RenewToken;
import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;

    // Access token 재발급 (Refresh Token을 받으면 재발급)
    @Operation(summary = "Access token 재발급 (Refresh Token을 받으면 Access token & Refresh Token 재발급, Refresh Token 만료 시 다시 로그인 진행해야 함)")
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<RenewToken>> renewToken(
            @RequestBody RefreshToken dto
    ){
        RenewToken token = authUseCase.renewToken(dto.refreshToken());
        return ResponseFactory.ok(token);
    }

    @Operation(summary = "로그 아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        authUseCase.logout(customOAuth2User.getMemberId());
        return ResponseFactory.noContent();
    }
}
