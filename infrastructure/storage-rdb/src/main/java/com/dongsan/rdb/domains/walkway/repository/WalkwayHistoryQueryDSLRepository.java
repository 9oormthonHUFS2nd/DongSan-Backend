package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.QWalkwayHistory;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .where(
                        walkwayHistory.member.id.eq(memberId),
                        walkwayHistory.walkway.id.eq(walkwayId),
                        walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0/3.0)),
                        walkwayHistory.isReviewed.eq(false)
                )
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }
}
