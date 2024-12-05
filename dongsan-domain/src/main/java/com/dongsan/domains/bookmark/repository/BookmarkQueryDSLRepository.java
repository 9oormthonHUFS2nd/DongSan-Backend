package com.dongsan.domains.bookmark.repository;

import static com.dongsan.domains.bookmark.entity.QMarkedWalkway.markedWalkway;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.QBookmark;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;

    public List<Bookmark> getBookmarks(Long bookmarkId, Long memberId, Integer limit) {
        return queryFactory.selectFrom(bookmark)
                .where(bookmark.member.id.eq(memberId), bookmarkIdLt(bookmarkId))
                .limit(limit)
                .orderBy(bookmark.id.desc())
                .fetch();
    }

    private BooleanExpression bookmarkIdLt(Long bookmarkId){
        // 조건 만족 안하면 null 반환
        // where 절에서 null은 무시된다.
        return bookmarkId != null ? bookmark.id.lt(bookmarkId) : null;
    }

    public List<Bookmark> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId) {
        return queryFactory.selectFrom(bookmark)
                .leftJoin(bookmark.markedWalkways, markedWalkway)
                .on(markedWalkway.walkway.id.eq(walkwayId))
                .where(bookmark.member.id.eq(memberId))
                .orderBy(bookmark.id.desc())
                .fetch();
    }
}
