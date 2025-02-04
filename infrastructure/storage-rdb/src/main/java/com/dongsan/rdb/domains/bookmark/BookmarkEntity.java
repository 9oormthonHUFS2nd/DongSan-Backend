package com.dongsan.rdb.domains.bookmark;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;  // 북마크 생성자

    @Column(nullable = false)
    private String name;

    @Builder
    private BookmarkEntity(String name, MemberEntity memberEntity){
        this.name = name;

        // 연관관계 매핑
        this.memberEntity = memberEntity;
    }

    public void rename(String name){
        this.name = name;
    }
}
