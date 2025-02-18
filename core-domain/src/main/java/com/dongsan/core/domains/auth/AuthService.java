package com.dongsan.core.domains.auth;

import com.dongsan.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public void deleteRefreshToken(Long memberId) {
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

    /**
     * 현재 value(refresh token) 과 비교할 refresh token이 동일한지 확인
     * @param memberId
     * @param refreshToken
     * @return 동일하면 true, 아니면 false, 키가 없으면 false
     */
    public boolean isRefreshTokenNotReplaced(Long memberId, String refreshToken){
        return authRepository.compareRefreshToken(memberId, refreshToken);
    }

    /**
     * 서버에 저장된 memberId의 refresh token 조회
     * @param memberId  key
     * @return  value (refresh token, 없으면 Null)
     */
    public String getRefreshToken(Long memberId){
        return authRepository.getRefreshToken(memberId);
    }
}
