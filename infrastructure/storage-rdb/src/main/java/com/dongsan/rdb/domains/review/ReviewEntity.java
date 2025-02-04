package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkwayEntity;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String content;

    @Builder
    private ReviewEntity(Integer rating, String content, MemberEntity memberEntity, WalkwayEntity walkwayEntity){
        this.rating = rating;
        this.content = content;

        // 연관관계 매핑
        this.memberEntity = memberEntity;
        this.walkwayEntity = walkwayEntity;
    }

}
