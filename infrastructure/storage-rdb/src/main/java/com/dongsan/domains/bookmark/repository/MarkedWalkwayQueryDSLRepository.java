package com.dongsan.domains.bookmark.repository;

import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.entity.QMarkedWalkway;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarkedWalkwayQueryDSLRepository {
    private final JPAQueryFactory queryFactory;

    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;

    public List<MarkedWalkway> getBookmarkWalkway(Long bookmarkId, Integer size, LocalDateTime lastCreatedAt) {
        return queryFactory.select(markedWalkway)
                .from(markedWalkway)
                .where(markedWalkway.bookmark.id.eq(bookmarkId), createdAtLt(lastCreatedAt))
                .orderBy(markedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * 북마크에 추가된 시간이 walkwayId가 북마크에 추가된 시간보다 작아야 한다. (markedBookmark의 createdAt이 더 작은 markedWalkay를 조회)
     * @param lastCreatedAt 마지막으로 가져온 markedBookmark의 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null;
    }

}
