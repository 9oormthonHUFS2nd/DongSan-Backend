package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.QLikedWalkway;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikedWalkwayQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QLikedWalkway likedWalkway = QLikedWalkway.likedWalkway;

    public List<LikedWalkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return queryFactory.selectFrom(likedWalkway)
                .where(likedWalkway.member.id.eq(memberId), createdAtLt(lastCreatedAt))
                .orderBy(likedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * lastCreatedAt 보다 작은 createdAt를 가진 likedWalkway를 조회하는 조건
     * @param lastCreatedAt 마지막으로 가져온 lastCreatedAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? likedWalkway.createdAt.lt(lastCreatedAt) : null;
    }

}
