package com.dongsan.rdb.domains.walkway.entity;

import com.dongsan.core.domains.walkway.WalkwayHistory;
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

@Entity
public class WalkwayHistoryEntity extends BaseEntity {
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
    private Double distance;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Boolean isReviewed;

    protected WalkwayHistoryEntity(){}

    public WalkwayHistoryEntity(MemberEntity memberEntity, WalkwayEntity walkwayEntity, Double distance, Integer time){
        this.memberEntity = memberEntity;
        this.walkwayEntity = walkwayEntity;
        this.distance = distance;
        this.time = time;
        this.isReviewed = false;
    }

    public void updateIsReviewed() {
        this.isReviewed = true;
    }

    public Long getId() {
        return id;
    }

    public WalkwayHistory toWalkwayHistory() {
        return new WalkwayHistory(id, memberEntity.getId(), walkwayEntity.toWalkway(), distance, time, isReviewed, getCreatedAt());
    }

}
