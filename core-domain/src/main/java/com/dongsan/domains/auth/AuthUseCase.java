package com.dongsan.domains.auth;

import com.dongsan.common.annotation.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthService authService;


    public void logout(Long memberId) {
        authService.logout(memberId);
    }
}
