package com.dongsan.rdb.domains.bookmark;

import com.dongsan.core.domains.bookmark.BookmarkRepository;
import com.dongsan.rdb.domains.bookmark.entity.QBookmark;
import com.dongsan.rdb.domains.bookmark.entity.QMarkedWalkway;
import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkCoreJpaRepository implements BookmarkRepository {

    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;
    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;


    public BookmarkCoreJpaRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    public List<BookmarkEntity> getBookmarks(BookmarkEntity lastBookmarkEntity, Long memberId, Integer size) {
        return queryFactory.selectFrom(bookmark)
                .where(bookmark.member.id.eq(memberId),
                        lastBookmarkEntity == null
                                ? null
                                : bookmarkCreatedAtLt(lastBookmarkEntity.getCreatedAt()))
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
    }

    private BooleanExpression bookmarkCreatedAtLt(LocalDateTime createdAt){
        return createdAt != null ? bookmark.createdAt.lt(createdAt) : null;
    }

    public List<BookmarksWithMarkedWalkwayDTO> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId, BookmarkEntity lastBookmarkEntity, Integer size) {
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
                        lastBookmarkEntity == null
                                ? null
                                : bookmarkCreatedAtLt(lastBookmarkEntity.getCreatedAt())
                )
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
    }



    public List<MarkedWalkway> getBookmarkWalkway(Long bookmarkId, Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return queryFactory.select(markedWalkway)
                .from(markedWalkway)
                .join(markedWalkway.walkway).fetchJoin()
                .where(markedWalkway.bookmark.id.eq(bookmarkId), markedBookmarkCreatedAtLt(lastCreatedAt),
                        markedWalkway.walkway.member.id.eq(memberId)
                                .or(markedWalkway.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .orderBy(markedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * 북마크에 추가된 시간이 walkwayId가 북마크에 추가된 시간보다 작아야 한다. (markedBookmark의 createdAt이 더 작은 markedWalkay를 조회)
     * @param lastCreatedAt 마지막으로 가져온 markedBookmark의 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression markedBookmarkCreatedAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null;
    }
}
