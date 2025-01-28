package com.dongsan.domains.auth;

import com.dongsan.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public void logout(Long memberId) {
        authRepository.deleteRefreshToken(memberId);
    }

    /**
     * refresh token 저장
     * @param memberId      key
     * @param refreshToken  value
     */
    public void saveRefreshToken(Long memberId, String refreshToken) {
        authRepository.saveRefreshToken(memberId, refreshToken);
    }
}
