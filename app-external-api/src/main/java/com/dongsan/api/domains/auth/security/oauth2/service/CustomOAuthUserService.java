package com.dongsan.api.domains.auth.security.oauth2.service;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.api.domains.auth.security.oauth2.dto.OAuth2Attributes;
import com.dongsan.api.domains.auth.security.oauth2.enums.SocialType;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.enums.MemberRole;
import com.dongsan.domains.member.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    /**
     * 사용자 정보 조회 <br>
     * - 신규 사용자의 경우 데이터 베이스에 저장
     * @param userRequest the user request
     * @return OAuth2User : SecurityContext 에 사용자 정보 저장
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = SocialType.from(registrationId);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, attributes);

        Member member = memberRepository.findByEmail(oAuth2Attributes.getEmail()).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(oAuth2Attributes.getEmail())
                    .nickname(oAuth2Attributes.getNickname())
                    .profileImageUrl(oAuth2Attributes.getProfileImage())
                    .role(MemberRole.ROLE_USER)
                    .build();

            log.info("[AUTH] 신규 회원 등록, 이메일 : %s".formatted(oAuth2Attributes.getEmail()));
            return memberRepository.save(newMember);
        });
        log.info("[AUTH] 로그인 이메일 : %s".formatted(member.getEmail()));

        return new CustomOAuth2User(member);
    }
}
