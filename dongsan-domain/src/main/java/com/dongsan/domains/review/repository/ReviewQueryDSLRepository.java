package com.dongsan.domains.review.repository;

import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.QReview;
import com.dongsan.domains.review.entity.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryDSLRepository{
    private final JPAQueryFactory queryFactory;

    private QReview review = QReview.review;

    public List<Review> getReviews(Integer limit, Long reviewId, Long memberId) {
        return queryFactory.selectFrom(review)
                .join(review.walkway).fetchJoin()
                .where(review.member.id.eq(memberId), reviewIdLt(reviewId))
                .limit(limit)
                .orderBy(review.createdAt.desc())
                .fetch();
    }

    /**
     * reviewId보다 작은 reviewId를 검색하는 조건
     * @param reviewId 마지막으로 가져온 reviewId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression reviewIdLt(Long reviewId){
        return reviewId != null ? review.id.lt(reviewId) : null;
    }

    public List<Review> getWalkwayReviewsLatest(Integer limit, Long reviewId, Long walkwayId) {
        return queryFactory.selectFrom(review)
                .join(review.walkway)
                .fetchJoin()
                .where(review.walkway.id.eq(walkwayId), reviewIdLt(reviewId))
                .limit(limit)
                .orderBy(review.createdAt.desc())
                .fetch();
    }

    public List<Review> getWalkwayReviewsRating(Integer limit, Long reviewId, Long walkwayId, Byte rating) {
        return queryFactory.selectFrom(review)
                .join(review.walkway)
                .fetchJoin()
                .where(review.walkway.id.eq(walkwayId),
                        review.rating.eq(rating).and(reviewIdLt(reviewId))
                                .or(review.rating.lt(rating))
                )
                .limit(limit)
                .orderBy(review.rating.desc(), review.id.desc())
                .fetch();
    }

    public List<RatingCount> getWalkwaysRating(Long walkwayId) {
        return queryFactory.select(
                        review.rating,
                        review.rating.count()
                )
                .from(review)
                .where(review.walkway.id.eq(walkwayId))
                .groupBy(review.rating)
                .fetch()
                .stream()
                .map(tuple -> new RatingCount(
                        tuple.get(review.rating),
                        tuple.get(review.rating.count())
                ))
                .toList();

    }
}
