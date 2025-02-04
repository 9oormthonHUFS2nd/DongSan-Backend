package com.dongsan.rdb.domains.member;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    private String profileImageUrl;

    private MemberRole role;

    @Builder
    private MemberEntity(String email, String nickname, String profileImageUrl, MemberRole role){
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

}
