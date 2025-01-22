package com.dongsan.domains.review.repository;

import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.QReview;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryDSLRepository{
    private final JPAQueryFactory queryFactory;

    private QReview review = QReview.review;

    /**
     * 사용자가 작성한 리뷰를 리뷰 작성시간 기준 내림차순으로 조회한다.
     * <p>
     *     1. 사용자가 작성한 리뷰를 조회한다. <br>
     *     2. 조회된 마지막 리뷰보다 더 일찍 작성된 리뷰를 조회한다. <br>
     *     3. 내가 등록한 산책로의 리뷰인 경우에는 산책로의 공개/비공개 여부 상관없이 리뷰를 조회한다. <br>
     *     4. 타인이 등록한 산책로의 리뷰인 경우에는 Public(공개) 상태의 산책로의 리뷰만 조회한다. <br>
     * </p>
     * @param size          최대 조회 갯수
     * @param lastCreatedAt 마지막으로 조회된 Review의 작성 시간
     * @param memberId      사용자 id
     * @return              사용자가 작성한 리뷰들
     */
    public List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return queryFactory.selectFrom(review)
                .join(review.walkway).fetchJoin()
                .where(review.member.id.eq(memberId), createdAtLt(lastCreatedAt),
                        review.walkway.member.id.eq(memberId).or(review.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .limit(size)
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

    /**
     * createdAt보다 작은 createdAt를 검색하는 조건
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt){
        return createdAt != null ? review.createdAt.lt(createdAt) : null;
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
