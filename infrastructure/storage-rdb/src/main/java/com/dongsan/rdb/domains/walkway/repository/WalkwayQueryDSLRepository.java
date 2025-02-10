package com.dongsan.rdb.domains.walkway.repository;


import static com.dongsan.domains.walkway.entity.QLikedWalkway.likedWalkway;

import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.domains.hashtag.entity.QHashtag;
import com.dongsan.domains.walkway.entity.QLikedWalkway;
import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkwayQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QWalkway walkway = QWalkway.walkway;

    public List<WalkwayEntity> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkway)
                .join(likedWalkway)
                .on(likedWalkway.walkway.eq(walkway))
                .where(
                        likedWalkway.member.id.eq(memberId),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(memberId)),
                        createdAtLt(lastCreatedAt)
                )
                .orderBy(walkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    public List<WalkwayEntity> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkway)
                .where(walkway.member.id.eq(memberId), createdAtLt(lastCreatedAt))
                .orderBy(walkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * walkwayId보다 작은 Id를 가진 walkway를 조회하는 조건 (즉, createdAt이 더 작은 walkway를 조회)
     *
     * @param walkwayId 마지막으로 가져온 walkwayId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression walkwayIdLt(Long walkwayId) {
        return walkwayId != null ? walkway.id.lt(walkwayId) : null;
    }

    /**
     * lastCreatedAt 보다 작은 createdAt 를 가진 walkway를 조회하는 조건
     *
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt) {
        return createdAt != null ? walkway.createdAt.lt(createdAt) : null;
    }

    // 검색 산책로 시작지점 거리 계산
    private BooleanExpression searchFilterDistance(Double longitude, Double latitude, Double distance) {
        return Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText(concat('POINT(', {1}, ' ', {2}, ')'), 4326)) <= {3}",
                walkway.startLocation,
                latitude,
                longitude,
                distance * 1000
        );
    }

    // 좋아요 순 검색
    public List<WalkwayEntity> searchWalkwaysLiked(Long userId, WalkwayEntity walkwayEntity, Double longitude, Double latitude, Double distance, int size) {

        return queryFactory.select(walkway)
                .from(walkway)
                .where(
                        this.searchFilterDistance(longitude, latitude, distance),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(userId)),
                        walkwayEntity == null
                                ? null
                                : walkway.likeCount.lt(walkwayEntity.getLikeCount())
                                        .or(walkway.likeCount.eq(walkwayEntity.getLikeCount())
                                                .and(createdAtLt(walkwayEntity.getCreatedAt()))
                                        )
                )
                .limit(size)
                .orderBy(walkway.likeCount.desc(), walkway.createdAt.desc())
                .fetch();
    }

    // 별점 순 검색
    public List<WalkwayEntity> searchWalkwaysRating(Long userId, WalkwayEntity walkwayEntity, Double longitude, Double latitude, Double distance, int size) {

        return queryFactory.select(walkway)
                .from(walkway)
                .where(
                        this.searchFilterDistance(longitude, latitude, distance),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(userId)),
                        walkwayEntity == null
                                ? null
                                : walkway.rating.lt(walkwayEntity.getRating())
                                        .or(walkway.rating.eq(walkwayEntity.getRating())
                                                .and(createdAtLt(walkwayEntity.getCreatedAt()))
                                        )
                )
                .limit(size)
                .orderBy(walkway.rating.desc(), walkway.createdAt.desc())
                .fetch();
    }

}
