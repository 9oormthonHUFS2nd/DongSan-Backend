package com.dongsan.domains.review.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private Walkway walkway;

    @Column(nullable = false)
    private Byte rating;

    @Column(nullable = false)
    private String content;

    @Builder
    private Review(Byte rating, String content, Member member, Walkway walkway){
        this.rating = rating;
        this.content = content;

        // 연관관계 매핑
        this.member = member;
        this.walkway = walkway;
    }

}
