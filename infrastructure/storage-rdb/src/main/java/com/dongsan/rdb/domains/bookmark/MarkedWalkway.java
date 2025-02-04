package com.dongsan.rdb.domains.bookmark;

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

    @Builder
    private MarkedWalkway(BookmarkEntity bookmarkEntity, WalkwayEntity walkwayEntity){
        this.bookmarkEntity = bookmarkEntity;
        this.walkwayEntity = walkwayEntity;
    }

}
