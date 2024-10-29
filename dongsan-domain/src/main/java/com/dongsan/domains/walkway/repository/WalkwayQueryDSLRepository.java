package com.dongsan.domains.walkway.repository;

import static com.dongsan.domains.walkway.entity.QLikedWalkway.likedWalkway;

import com.dongsan.domains.walkway.entity.QWalkway;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkwayQueryDSLRepository {
    private final JPAQueryFactory queryFactory;

    private QWalkway walkway = QWalkway.walkway;

    public Tuple getWalkwayWithLiked(Long walkwayId, Long memberId) {
        return queryFactory
                .select(walkway, likedWalkway)
                .from(walkway)
                .leftJoin(likedWalkway)
                .on(likedWalkway.walkway.id.eq(walkwayId).and(likedWalkway.member.id.eq(memberId)))
                .where(walkway.id.eq(walkwayId))
                .fetchOne();
    }
}
