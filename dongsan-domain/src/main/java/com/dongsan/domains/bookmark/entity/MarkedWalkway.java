package com.dongsan.domains.bookmark.entity;

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
public class MarkedWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private Walkway walkway;

    @Builder
    private MarkedWalkway(Bookmark bookmark, Walkway walkway){
        this.bookmark = bookmark;
        this.walkway = walkway;
    }

}
