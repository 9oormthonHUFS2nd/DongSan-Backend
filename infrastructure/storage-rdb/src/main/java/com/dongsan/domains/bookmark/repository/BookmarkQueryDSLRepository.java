package com.dongsan.domains.bookmark.repository;

import static com.dongsan.domains.bookmark.entity.QMarkedWalkway.markedWalkway;

import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.QBookmark;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;

    public List<Bookmark> getBookmarks(Bookmark lastBookmark, Long memberId, Integer size) {
        return queryFactory.selectFrom(bookmark)
                .where(bookmark.member.id.eq(memberId),
                        lastBookmark == null
                                ? null
                                : createdAtLt(lastBookmark.getCreatedAt()))
                .limit(size)
                .orderBy(bookmark.id.desc())
                .fetch();
    }

    private BooleanExpression createdAtLt(LocalDateTime createdAt){
        return createdAt != null ? bookmark.createdAt.lt(createdAt) : null;
    }

    public List<BookmarksWithMarkedWalkwayDTO> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId, Bookmark lastBookmark, Integer size) {
        return queryFactory.select(Projections.constructor(
                        BookmarksWithMarkedWalkwayDTO.class,
                        bookmark.id,
                        bookmark.member.id,
                        bookmark.name,
                        markedWalkway.id
                ))
                .from(bookmark)
                .leftJoin(markedWalkway)
                .on(markedWalkway.walkway.id.eq(walkwayId).and(markedWalkway.bookmark.id.eq(bookmark.id)))
                .where(bookmark.member.id.eq(memberId),
                        lastBookmark == null
                                ? null
                                : createdAtLt(lastBookmark.getCreatedAt())
                )
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
    }
}
