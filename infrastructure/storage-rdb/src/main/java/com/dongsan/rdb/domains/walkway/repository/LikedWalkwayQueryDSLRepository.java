package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.rdb.domains.walkway.entity.QLikedWalkway;
import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkway;
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

    /**
     * 사용자가 좋아요한 산책로를 좋아요를 누른 시점을 기준으로 내림차순 정렬한다.
     * <p>
     *     1. 사용자가 좋아요한 산책로를 조회한다. <br>
     *     2. 내가 등록한 산책로인 경우에는 산책로의 공개/비공개 여부 상관없이 조회한다. <br>
     *     3. 타인이 등록한 산책로인 경우에는 Public(공개) 상태의 산책로만 조회한다. <br>
     * </p>
     * @param memberId      사용자 id
     * @param size          최대 조회 갯수
     * @param lastCreatedAt 마지막으로 조회된 산책로의 좋아요 시각
     * @return              사용자가 좋아요를 누른 산책로
     */
    public List<LikedWalkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return queryFactory
                .selectFrom(likedWalkway)
                .join(likedWalkway.walkway).fetchJoin()
                .where(likedWalkway.member.id.eq(memberId), createdAtLt(lastCreatedAt),
                        likedWalkway.walkway.member.id.eq(memberId).or(likedWalkway.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
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
