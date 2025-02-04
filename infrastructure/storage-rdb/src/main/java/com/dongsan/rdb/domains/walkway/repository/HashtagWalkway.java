package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkwayEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashtagEntity hashtagEntity;

    @Builder
    private HashtagWalkway(WalkwayEntity walkwayEntity, HashtagEntity hashtagEntity){
        this.walkwayEntity = walkwayEntity;
        this.hashtagEntity = hashtagEntity;
    }
}
