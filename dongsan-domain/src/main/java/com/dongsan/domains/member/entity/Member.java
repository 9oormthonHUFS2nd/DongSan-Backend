package com.dongsan.domains.member.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    private String profileImageUrl;

    @Builder
    private Member(String email, String nickname, String profileImageUrl){
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

}
