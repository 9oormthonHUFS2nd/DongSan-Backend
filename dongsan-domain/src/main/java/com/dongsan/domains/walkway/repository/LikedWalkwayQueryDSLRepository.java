package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.QLikedWalkway;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikedWalkwayQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QLikedWalkway likedWalkway = QLikedWalkway.likedWalkway;

    public List<LikedWalkway> getUserLikedWalkway(Long memberId, Integer size, Long likedWalkwayId){
        return queryFactory.selectFrom(likedWalkway)
                .where(likedWalkway.member.id.eq(memberId), likedWalkwayLt(likedWalkwayId))
                .orderBy(likedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * likedWalkwayId보다 작은 Id를 가진 likedWalkway를 조회하는 조건 (즉, createdAt이 더 작은 likedWalkway를 조회)
     * @param likedWalkwayId 마지막으로 가져온 likedWalkwayId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression likedWalkwayLt(Long likedWalkwayId){
        return likedWalkwayId != null ? likedWalkway.id.lt(likedWalkwayId) : null;
    }

}
