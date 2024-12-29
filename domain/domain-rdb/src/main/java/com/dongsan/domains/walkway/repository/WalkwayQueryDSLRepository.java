package com.dongsan.domains.walkway.repository;

import static com.dongsan.domains.hashtag.entity.QHashtag.hashtag;
import static com.dongsan.domains.hashtag.entity.QHashtagWalkway.hashtagWalkway;
import static com.dongsan.domains.walkway.entity.QLikedWalkway.likedWalkway;

import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
                = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", searchWalkwayPopular.longitude(),
                searchWalkwayPopular.latitude());

        return queryFactory.selectFrom(walkway)
                .join(walkway.hashtagWalkways, hashtagWalkway)
                .fetchJoin()
                .join(hashtagWalkway.hashtag, hashtag)
                .fetchJoin()
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                searchWalkwayPopular.distance()
                        ),
                        walkway.in(
                                JPAExpressions
                                        .selectFrom(walkway)
                                        .join(hashtagWalkway)
                                        .on(hashtagWalkway.walkway.eq(walkway))
                                        .join(hashtag)
                                        .on(hashtagWalkway.hashtag.eq(hashtag))
                                        .where(walkwayHashtagIn(searchWalkwayPopular.hashtags()))
                        ),
                        walkwaySearchLikeEqLt(searchWalkwayPopular.walkway()),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                )
                .orderBy(walkway.likeCount.desc(), walkway.createdAt.desc())
                .limit(searchWalkwayPopular.size())
                .fetch();
    }

    public List<Walkway> getWalkwaysRating(
            SearchWalkwayRating searchWalkwayRating
    ) {
        String point
                = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", searchWalkwayRating.longitude(),
                searchWalkwayRating.latitude());

        return queryFactory.selectFrom(walkway)
                .join(walkway.hashtagWalkways, hashtagWalkway)
                .fetchJoin()
                .join(hashtagWalkway.hashtag, hashtag)
                .fetchJoin()
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                searchWalkwayRating.distance()
                        ),
                        walkway.in(
                                JPAExpressions
                                        .selectFrom(walkway)
                                        .join(hashtagWalkway)
                                        .on(hashtagWalkway.walkway.eq(walkway))
                                        .join(hashtag)
                                        .on(hashtagWalkway.hashtag.eq(hashtag))
                                        .where(walkwayHashtagIn(searchWalkwayRating.hashtags()))
                        ),
                        walkwaySearchRatingEqLt(searchWalkwayRating.walkway()),
                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
                )
                .orderBy(walkway.rating.desc(), walkway.createdAt.desc())
                .limit(searchWalkwayRating.size())
                .fetch();
    }

    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
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

    public Optional<Walkway> getWalkwayWithHashtag(Long walkwayId) {
        return Optional.ofNullable(queryFactory.selectFrom(walkway)
                .join(walkway.hashtagWalkways, hashtagWalkway)
                .fetchJoin()
                .join(hashtagWalkway.hashtag)
                .fetchJoin()
                .join(walkway.member)
                .fetchJoin()
                .where(walkway.id.eq(walkwayId))
                .fetchOne());
    }

    private BooleanExpression walkwayHashtagIn(List<String> hashtags) {
        return hashtags.isEmpty() ? null : hashtag.name.in(hashtags);
    }

    private BooleanExpression walkwaySearchRatingEqLt(Walkway lastWalkway) {
        if (lastWalkway == null) {
            return null;
        }
        return walkway.createdAt.lt(lastWalkway.getCreatedAt())
                .or(walkway.rating.eq(lastWalkway.getRating()))
                .or(walkway.rating.lt(lastWalkway.getRating()));
    }

    private BooleanExpression walkwaySearchLikeEqLt(Walkway lastWalkway) {
        if (lastWalkway == null) {
            return null;
        }
        return walkway.createdAt.lt(lastWalkway.getCreatedAt())
                .or(walkway.likeCount.eq(lastWalkway.getLikeCount()))
                .or(walkway.likeCount.lt(lastWalkway.getLikeCount()));
    }

}
