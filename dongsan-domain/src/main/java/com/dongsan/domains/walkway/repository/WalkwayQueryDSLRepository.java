package com.dongsan.domains.walkway.repository;

import static com.dongsan.domains.hashtag.entity.QHashtag.hashtag;
import static com.dongsan.domains.hashtag.entity.QHashtagWalkway.hashtagWalkway;
import static com.dongsan.domains.walkway.entity.QLikedWalkway.likedWalkway;

import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkwayQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    private QWalkway walkway = QWalkway.walkway;

    public List<Walkway> getWalkwaysPopular(
            Long userId,
            Double longitude,
            Double latitude,
            int distance,
            List<String> hashtags,
            Long lastId,
            Integer lastLikes,
            int size
    ) {
        String point = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", longitude, latitude);

        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, hashtag)
                .leftJoin(walkway.likedWalkways, likedWalkway)
                .on(likedWalkway.member.id.eq(userId))
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                distance
                        ),
                        hashtag.name.in(hashtags),
                        walkway.likeCount.lt(lastLikes)
                                .or(walkway.likeCount.eq(lastLikes).and(walkway.id.lt(lastId)))
                )
                .orderBy(walkway.likeCount.desc(), walkway.id.desc())
                .limit(size)
                .fetch();
    }

    public List<Walkway> getWalkwaysRating(
            Long userId,
            Double latitude,
            Double longitude,
            int distance,
            List<String> hashtags,
            Long lastId,
            Double lastRating,
            int size
    ) {
        String point = String.format("ST_GeomFromText('POINT(%f %f)', 4326)", longitude, latitude);

        return queryFactory.selectFrom(walkway)
                .leftJoin(walkway.hashtagWalkways, hashtagWalkway)
                .leftJoin(hashtagWalkway.hashtag, hashtag)
                .leftJoin(walkway.likedWalkways, likedWalkway)
                .on(likedWalkway.member.id.eq(userId))
                .where(
                        Expressions.booleanTemplate(
                                "ST_Distance_Sphere({0}," + point + ") <= {1}",
                                walkway.startLocation,
                                distance
                        ),
                        hashtag.name.in(hashtags),
                        walkway.rating.lt(lastRating)
                                .or(walkway.rating.eq(lastRating).and(walkway.id.lt(lastId)))
                )
                .limit(size)
                .orderBy(walkway.rating.desc(), walkway.id.desc())
                .fetch();
    }



}
