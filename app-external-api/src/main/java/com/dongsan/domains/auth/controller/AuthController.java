package com.dongsan.domains.auth.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.domains.auth.AuthUseCase;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;

    // Refresh token 재발급


    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        authUseCase.logout(customOAuth2User.getMemberId());
        return ResponseFactory.noContent();
    }
}
