package com.dongsan.domains.review.repository;

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

    // TODO : 쿼리 확인 필요 (member join 안해도 쿼리 발생 하는지, 안하는지)
    public List<Review> getReviews(Integer limit, Long reviewId, Long memberId) {
        return queryFactory.selectFrom(review)
                //.join(review.member)
                .join(review.walkway).fetchJoin()
                .where(review.member.id.eq(memberId), reviewIdLt(reviewId))
                .limit(limit)
                .orderBy(review.createdAt.desc())
                .fetch();
    }

    private BooleanExpression reviewIdLt(Long reviewId){
        // 조건 만족 안하면 null 반환
        // where 절에서 null은 무시된다.
        return reviewId != null ? review.id.lt(reviewId) : null;
    }
}
