package com.dongsan.domains.hashtag.repository;

import static com.dongsan.domains.hashtag.entity.QHashtagWalkway.hashtagWalkway;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.QHashtag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HashtagDSLRepository {
    private final JPAQueryFactory queryFactory;

    private QHashtag hashtag = QHashtag.hashtag;

    public List<Hashtag> getHashtagsByWalkwayId(Long walkwayId) {
        return queryFactory
                .select(hashtag)
                .from(hashtag)
                .join(hashtagWalkway)
                .on(hashtagWalkway.walkway.id.eq(walkwayId).and(hashtagWalkway.hashtag.eq(hashtag)))
                .fetch();
    }
}
