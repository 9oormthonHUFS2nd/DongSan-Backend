package com.dongsan.rdb.domains.walkway.entity;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class LikedWalkwayEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkwayEntity;

    protected LikedWalkwayEntity(){}

    // 생성 시 연관관계 매핑 진행
    public LikedWalkwayEntity(MemberEntity memberEntity, WalkwayEntity walkwayEntity){
        this.memberEntity = memberEntity;
        this.walkwayEntity = walkwayEntity;
    }

    public Long getId() {
        return id;
    }
}
