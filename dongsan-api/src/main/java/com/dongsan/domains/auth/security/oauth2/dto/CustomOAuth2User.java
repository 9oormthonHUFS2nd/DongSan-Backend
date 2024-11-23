package com.dongsan.domains.auth.security.oauth2.dto;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.enums.MemberRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final Member member;

    @Override
    public <A> A getAttribute(String name) {
        return (A) getAttributes().get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", member.getEmail(),
                "nickname", member.getNickname(),
                "profileImageUrl", member.getProfileImageUrl(),
                "role", member.getRole()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> member.getRole()
                .getDescription());
        return collection;
    }

    @Override
    public String getName() {
        return member.getEmail();
    }

    public Long getMemberId(){
        return member.getId();
    }

    public String getEmail(){
        return member.getEmail();
    }

    public String getNickname(){
        return member.getNickname();
    }

    public String getProfileImageUrl(){
        return member.getProfileImageUrl();
    }

    public MemberRole getRole(){
        return member.getRole();
    }
}
