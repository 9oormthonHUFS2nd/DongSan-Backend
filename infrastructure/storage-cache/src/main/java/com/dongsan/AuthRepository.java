package com.dongsan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthRepository {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * refresh token 저장
     * 키가 존재하는 경우에는 값을 업데이트하고, 존재하지 않으면 새로운 키-값 쌍을 생성한다.
     * @param memberId      key
     * @param refreshToken  value
     */
    public void saveRefreshToken(Long memberId, String refreshToken) {
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.opsForValue().set(key, refreshToken);
    }

    /**
     * refresh token 삭제
     * @param memberId  key
     */
    public void deleteRefreshToken(Long memberId){
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.delete(key);
    }

    private String generateRefreshTokenKey(Long memberId){
        return "refreshToken:" + memberId;
    }


}
