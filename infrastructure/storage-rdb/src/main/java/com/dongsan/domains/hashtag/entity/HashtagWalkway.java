package com.dongsan.domains.hashtag.entity;

import com.dongsan.domains.common.entity.BaseEntity;
import com.dongsan.domains.walkway.entity.Walkway;
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
    private Walkway walkway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Builder
    private HashtagWalkway(Walkway walkway, Hashtag hashtag){
        this.walkway = walkway;
        this.hashtag = hashtag;
    }
}
