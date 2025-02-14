package com.dongsan.rdb.domains.bookmark;

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
public class MarkedWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private BookmarkEntity bookmarkEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkwayEntity;

    protected MarkedWalkway(){}

    public MarkedWalkway(BookmarkEntity bookmarkEntity, WalkwayEntity walkwayEntity){
        this.bookmarkEntity = bookmarkEntity;
        this.walkwayEntity = walkwayEntity;
    }

}
