package com.dongsan.domains.auth.security.oauth2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    //GOOGLE("google")
    ;

    private final String registrationId;

    public static SocialType from(String registrationId){
        for(SocialType socialType:SocialType.values()){
            if(socialType.getRegistrationId().equals(registrationId)){
                return socialType;
            }
        }
        throw new OAuth2AuthenticationException("존재하지 않는 OAuth registrationId 입니다. : %s".formatted(registrationId));
    }
}
