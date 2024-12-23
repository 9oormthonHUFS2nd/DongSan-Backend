package com.dongsan.domains.walkway.repository;

import static com.dongsan.domains.hashtag.entity.QHashtag.hashtag;
import static com.dongsan.domains.hashtag.entity.QHashtagWalkway.hashtagWalkway;
import static com.dongsan.domains.walkway.entity.QLikedWalkway.likedWalkway;

import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
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

    public Walkway getUserWalkwayWithHashtagAndLike(Long userId, Long walkwayId) {
        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, hashtag)
                .leftJoin(walkway.likedWalkways, likedWalkway)
                .on(likedWalkway.member.id.eq(userId))
                .where(walkway.id.eq(walkwayId))
                .fetchOne();
    }

    public List<Walkway> getWalkwaysPopular(
            SearchWalkwayPopular searchWalkwayPopular
    ) {
        String point
                = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", searchWalkwayPopular.longitude(), searchWalkwayPopular.latitude());

        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, hashtag)
                .leftJoin(walkway.likedWalkways, likedWalkway)
                .on(likedWalkway.member.id.eq(searchWalkwayPopular.userId()))
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                searchWalkwayPopular.distance()
                        ),
                        hashtag.name.in(searchWalkwayPopular.hashtags()),
                        walkway.likeCount.eq(searchWalkwayPopular.lastLikes()).and(walkwayIdLt(searchWalkwayPopular.lastId()))
                                        .or(walkway.likeCount.lt(searchWalkwayPopular.lastLikes()))
                )
                .orderBy(walkway.likeCount.desc(), walkway.id.desc())
                .limit(searchWalkwayPopular.size())
                .fetch();
    }

    public List<Walkway> getWalkwaysRating(
            SearchWalkwayRating searchWalkwayRating
    ) {
        String point
                = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", searchWalkwayRating.longitude(), searchWalkwayRating.latitude());

        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, hashtag)
                .leftJoin(walkway.likedWalkways, likedWalkway)
                .on(likedWalkway.member.id.eq(searchWalkwayRating.userId()))
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                searchWalkwayRating.distance()
                        ),
                        hashtag.name.in(searchWalkwayRating.hashtags()),
                        walkway.rating.eq(searchWalkwayRating.lastRating()).and(walkwayIdLt(searchWalkwayRating.lastId()))
                                .or(walkway.rating.lt(searchWalkwayRating.lastRating()))
                )
                .limit(searchWalkwayRating.size())
                .orderBy(walkway.rating.desc(), walkway.id.desc())
                .fetch();
    }

    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return queryFactory.selectFrom(walkway)
                .where(walkway.member.id.eq(memberId), createdAtLt(lastCreatedAt))
                .orderBy(walkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * walkwayId보다 작은 Id를 가진 walkway를 조회하는 조건 (즉, createdAt이 더 작은 walkway를 조회)
     * @param walkwayId 마지막으로 가져온 walkwayId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression walkwayIdLt(Long walkwayId){
        return walkwayId != null ? walkway.id.lt(walkwayId) : null;
    }

    /**
     * lastCreatedAt 보다 작은 createdAt 를 가진 walkway를 조회하는 조건
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt){
        return createdAt != null ? walkway.createdAt.lt(createdAt) : null;
    }

    public Walkway getWalkwayWithHashtag(Long walkwayId) {
        return queryFactory.selectFrom(walkway)
                .join(walkway.hashtagWalkways, hashtagWalkway)
                .fetchJoin()
                .join(hashtagWalkway.hashtag)
                .fetchJoin()
                .join(walkway.member)
                .fetchJoin()
                .where(walkway.id.eq(walkwayId))
                .fetchOne();
    }

}
