package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.QWalkwayHistory;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkwayHistoryQueryDSLRepository {
    private final JPAQueryFactory queryFactory;

    private QWalkwayHistory walkwayHistory = QWalkwayHistory.walkwayHistory;

    public List<WalkwayHistory> getCanReviewWalkwayHistories(Long walkwayId, Long memberId) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway).fetchJoin()
                .where(
                        walkwayHistory.member.id.eq(memberId),
                        walkwayHistory.walkway.id.eq(walkwayId),
                        walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0/3.0)),
                        walkwayHistory.isReviewed.eq(false)
                )
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    public List<WalkwayHistory> getUserCanReviewWalkwayHistories(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway).fetchJoin()
                .where(
                        walkwayHistory.member.id.eq(memberId),
                        walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0/3.0)),
                        walkwayHistory.isReviewed.eq(false),
                        walkwayHistory.walkway.exposeLevel.eq(ExposeLevel.PUBLIC),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? walkwayHistory.createdAt.lt(lastCreatedAt) : null;
    }
}
