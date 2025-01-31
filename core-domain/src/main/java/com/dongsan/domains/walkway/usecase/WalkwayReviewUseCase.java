package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.code.WalkwayHistoryErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayRatingResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.dongsan.domains.walkway.enums.ReviewSort;
import com.dongsan.domains.walkway.mapper.ReviewMapper;
import com.dongsan.domains.walkway.service.ReviewCommandService;
import com.dongsan.domains.walkway.service.ReviewQueryService;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayReviewUseCase {

    private final ReviewCommandService reviewCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;
    private final ReviewQueryService reviewQueryService;
    private final WalkwayCommandService walkwayCommandService;
    private final WalkwayHistoryQueryService walkwayHistoryQueryService;
    private final WalkwayHistoryCommandService walkwayHistoryCommandService;

    @Transactional
    public CreateReviewResponse createReview(Long memberId, Long walkwayId, CreateReviewRequest createReviewRequest) {
        Member member = memberQueryService.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        WalkwayHistory walkwayHistory = walkwayHistoryQueryService.findByWalkwayAndMember(walkwayId, memberId);

        if (walkway.getDistance() * 2/3 > walkwayHistory.getDistance()) {
            throw new CustomException(WalkwayHistoryErrorCode.NOT_ENOUGH_DISTANCE);
        }

        if(walkwayHistory.getIsReviewed()) {
            throw new CustomException(WalkwayHistoryErrorCode.ALREADY_REVIEWED);
        }

        walkwayHistory.updateIsReviewed();
        walkwayHistoryCommandService.createWalkwayHistory(walkwayHistory);

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
