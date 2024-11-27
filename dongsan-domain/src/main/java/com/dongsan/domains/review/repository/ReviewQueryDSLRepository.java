package com.dongsan.domains.review.repository;

import com.dongsan.domains.review.entity.QReview;
import com.dongsan.domains.review.entity.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
