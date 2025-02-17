package com.dongsan.rdb.domains.bookmark;

import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MarkedWalkwayEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private BookmarkEntity bookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkway;

    protected MarkedWalkwayEntity(){}

    public MarkedWalkwayEntity(BookmarkEntity bookmark, WalkwayEntity walkway){
        this.bookmark = bookmark;
        this.walkway = walkway;
    }

    public MarkedWalkway toMarkedWalkway(){
        return new MarkedWalkway(walkway.getId(), walkway.getName(), getCreatedAt(), walkway.getDistance(), walkway.getHashtags(),
                walkway.getCourseImageUrl());
    }

}
