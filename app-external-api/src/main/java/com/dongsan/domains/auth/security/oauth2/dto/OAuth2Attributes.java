package com.dongsan.domains.auth.security.oauth2.dto;

import com.dongsan.domains.auth.security.oauth2.enums.SocialType;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OAuth2Attributes {
    private final String email;
    private final String nickname;
    private final String profileImage;

    @Builder
    private OAuth2Attributes(String email, String nickname, String profileImage){
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static OAuth2Attributes of(SocialType socialType, Map<String, Object> attributes){
        return switch (socialType){
            case KAKAO -> ofKakao(attributes);
            case NAVER -> ofNaver(attributes);
            //case GOOGLE -> ;
        };
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes){
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
        return OAuth2Attributes.builder()
                .email((String) kakaoAccount.get("email"))
                .nickname(profile.get("nickname"))
                .profileImage(profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes){
        Map<String, String> response = (Map<String, String>) attributes.get("response");
        return OAuth2Attributes.builder()
                .email(response.get("email"))
                .nickname(response.get("nickname"))
                .profileImage(response.get("profile_image"))
                .build();
    }

//    private static OAuth2Registration ofGoogle(){
//
//    }
}
