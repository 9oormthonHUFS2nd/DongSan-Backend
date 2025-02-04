package com.dongsan.rdb.domains.walkway.repository;

import static com.dongsan.rdb.domains.hashtag.entity.QHashtagWalkway.hashtagWalkway;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.dongsan.rdb.domains.hashtag.entity.QHashtag;
import com.dongsan.rdb.domains.walkway.entity.QLikedWalkway;
import com.dongsan.rdb.domains.walkway.entity.QWalkway;
import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
import com.dongsan.rdb.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
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

    public WalkwayEntity getUserWalkwayWithHashtagAndLike(Long userId, Long walkwayId) {
        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, QHashtag.hashtag)
                .leftJoin(walkway.likedWalkways, QLikedWalkway.likedWalkway)
                .on(QLikedWalkway.likedWalkway.member.id.eq(userId))
                .where(walkway.id.eq(walkwayId),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(userId))
                )
                .fetchOne();
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
    private BooleanExpression searchFilterDistance(SearchWalkwayQuery searchWalkwayRequest) {
        return Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText(concat('POINT(', {1}, ' ', {2}, ')'), 4326)) <= {3}",
                walkway.startLocation,
                searchWalkwayRequest.latitude(),
                searchWalkwayRequest.longitude(),
                searchWalkwayRequest.distance() * 1000
        );
    }

    // 검색 기본 쿼리 (조인, 기본 조건)
    private JPAQuery<?> searchBaseQuery(SearchWalkwayQuery searchWalkwayRequest, List<Long> walkwayIds) {
        return queryFactory
                .from(walkway)
                .leftJoin(hashtagWalkway)
                .on(hashtagWalkway.walkway.eq(walkway))
                .leftJoin(hashtagWalkway.hashtag, QHashtag.hashtag)
                .leftJoin(QLikedWalkway.likedWalkway)
                .on(QLikedWalkway.likedWalkway.member.id.eq(searchWalkwayRequest.userId()), QLikedWalkway.likedWalkway.walkway.eq(walkway))
                .where(
                        walkway.id.in(walkwayIds)
                );
    }

    // 검색 DTO 변환
    private List<SearchWalkwayResult> transformToSearchWalkwayResult(JPAQuery<?> query) {
        return query.transform(groupBy(walkway.id).list(
                        Projections.constructor(
                                SearchWalkwayResult.class,
                                walkway.id,
                                walkway.name,
                                walkway.distance,
                                GroupBy.list(QHashtag.hashtag.name),
                                QLikedWalkway.likedWalkway.isNotNull(),
                                walkway.likeCount,
                                walkway.reviewCount,
                                walkway.rating,
                                walkway.courseImageUrl,
                                walkway.startLocation,
                                walkway.createdAt
                        )
                )
        );
    }

    // 좋아요 순 검색
    public List<SearchWalkwayResult> searchWalkwaysLiked(
            SearchWalkwayQuery searchWalkwayRequest
    ) {

        List<Long> walkwayIds = queryFactory.select(walkway.id)
                .from(walkway)
                .where(
                        this.searchFilterDistance(searchWalkwayRequest),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(searchWalkwayRequest.userId())),
                        searchWalkwayRequest.walkwayEntity() == null
                                ? null
                                : walkway.likeCount.lt(searchWalkwayRequest.walkwayEntity().getLikeCount())
                                                .or(walkway.likeCount.eq(searchWalkwayRequest.walkwayEntity().getLikeCount())
                                                        .and(createdAtLt(searchWalkwayRequest.walkwayEntity().getCreatedAt()))
                                                )
                )
                .limit(searchWalkwayRequest.size())
                .orderBy(walkway.likeCount.desc(), walkway.createdAt.desc())
                .fetch();

        JPAQuery<?> query = this.searchBaseQuery(searchWalkwayRequest, walkwayIds)
                .orderBy(walkway.likeCount.desc(), walkway.createdAt.desc());

        return this.transformToSearchWalkwayResult(query);
    }

    // 별점 순 검색
    public List<SearchWalkwayResult> searchWalkwaysRating(
            SearchWalkwayQuery searchWalkwayRequest
    ) {

        List<Long> walkwayIds = queryFactory.select(walkway.id)
                .from(walkway)
                .where(
                        this.searchFilterDistance(searchWalkwayRequest),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.member.id.eq(searchWalkwayRequest.userId())),
                        searchWalkwayRequest.walkwayEntity() == null
                                ? null
                                : walkway.rating.lt(searchWalkwayRequest.walkwayEntity().getRating())
                                                .or(walkway.rating.eq(searchWalkwayRequest.walkwayEntity().getRating())
                                                        .and(createdAtLt(searchWalkwayRequest.walkwayEntity().getCreatedAt()))
                                                )
                )
                .limit(searchWalkwayRequest.size())
                .orderBy(walkway.rating.desc(), walkway.createdAt.desc())
                .fetch();

        JPAQuery<?> query = this.searchBaseQuery(searchWalkwayRequest, walkwayIds)
                .orderBy(walkway.rating.desc(), walkway.createdAt.desc());

        return this.transformToSearchWalkwayResult(query);
    }

}
