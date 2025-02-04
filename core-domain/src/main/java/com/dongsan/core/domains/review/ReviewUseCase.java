package com.dongsan.core.domains.review;

import com.dongsan.core.domains.user.MemberQueryService;
import com.dongsan.core.domains.walkway.service.WalkwayCommandService;
import com.dongsan.core.domains.walkway.service.WalkwayQueryService;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.core.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.core.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.core.domains.walkway.dto.response.GetWalkwayRatingResponse;
import com.dongsan.core.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.dongsan.core.domains.walkway.enums.ReviewSort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class ReviewUseCase {

    private final ReviewCommandService reviewCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;
    private final ReviewQueryService reviewQueryService;
    private final WalkwayCommandService walkwayCommandService;

    @Transactional
    public CreateReviewResponse createReview(Long memberId, Long walkwayId, CreateReviewRequest createReviewRequest) {
        Member member = memberQueryService.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        Review review = ReviewMapper.toReview(createReviewRequest, walkway, member);
        review = reviewCommandService.createReview(review);

        List<RatingCount> ratingCounts = reviewQueryService.getWalkwaysRating(walkwayId);
        walkway.updateRatingAndReviewCount(ratingCounts);
        walkwayCommandService.createWalkway(walkway);

        return ReviewMapper.toCreateReviewResponse(review);
    }

    @Transactional(readOnly = true)
    public GetWalkwayReviewsResponse getWalkwayReviews(String type, Long lastId, Long walkwayId, Integer size, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        if (walkway.getExposeLevel().equals(ExposeLevel.PRIVATE) && !walkway.getMember().getId().equals(memberId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_PRIVATE);
        }

        Review review = null;
        if (lastId != null) {
            review = reviewQueryService.getReview(lastId);
        }

        ReviewSort sort = ReviewSort.typeOf(type);
        List<Review> reviews = reviewQueryService.getWalkwayReviews(size, review, walkway, sort);

        return ReviewMapper.toGetWalkwayReviewsResponse(reviews, size);
    }

    @Transactional(readOnly = true)
    public GetWalkwayRatingResponse getWalkwayRating(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        if (walkway.getExposeLevel().equals(ExposeLevel.PRIVATE) && !walkway.getMember().getId().equals(memberId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_PRIVATE);
        }

        List<RatingCount> ratingCounts = reviewQueryService.getWalkwaysRating(walkwayId);

        return ReviewMapper.toGetWalkwayRatingResponse(ratingCounts, walkway);
    }
}
