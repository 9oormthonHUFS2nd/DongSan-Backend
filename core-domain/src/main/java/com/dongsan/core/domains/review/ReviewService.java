package com.dongsan.core.domains.review;

import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.core.domains.walkway.enums.ReviewSort;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import com.dongsan.core.domains.walkway.service.WalkwayWriter;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
import java.lang.reflect.Member;
import org.springframework.transaction.annotation.Transactional;

public class ReviewService {

    private final ReviewWriter reviewWriter;
    private final MemberReader memberReader;
    private final WalkwayReader walkwayQueryService;
    private final ReviewReader reviewReader;
    private final WalkwayWriter walkwayCommandService;
    private final ReviewCommandService reviewCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;
    private final ReviewQueryService reviewQueryService;
    private final WalkwayCommandService walkwayCommandService;
    private final WalkwayHistoryQueryService walkwayHistoryQueryService;
    private final WalkwayHistoryCommandService walkwayHistoryCommandService;

    @Transactional
    public CreateReviewResponse createReview(Long memberId, Long walkwayId, CreateReviewRequest createReviewRequest) {
        Member member = memberReader.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        WalkwayHistory walkwayHistory
                = walkwayHistoryQueryService.getById(createReviewRequest.walkwayHistoryId());

        if (!walkwayHistory.getWalkway().equals(walkway) || !walkwayHistory.getMember().equals(member)) {
            throw new CustomException(WalkwayHistoryErrorCode.INVALID_ACCESS);
        }

        if (walkway.getDistance() * 2/3 > walkwayHistory.getDistance()) {
            throw new CustomException(WalkwayHistoryErrorCode.NOT_ENOUGH_DISTANCE);
        }

        if(walkwayHistory.getIsReviewed()) {
            throw new CustomException(WalkwayHistoryErrorCode.ALREADY_REVIEWED);
        }

        walkwayHistory.updateIsReviewed();
        walkwayHistoryCommandService.createWalkwayHistory(walkwayHistory);

        Review review = ReviewMapper.toReview(createReviewRequest, walkway, member);
        review = reviewWriter.createReview(review);

        List<RatingCount> ratingCounts = reviewReader.getWalkwaysRating(walkwayId);
        walkway.updateRatingAndReviewCount(ratingCounts);
        walkwayCommandService.saveWalkway(walkway);

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
            review = reviewReader.getReview(lastId);
        }

        ReviewSort sort = ReviewSort.typeOf(type);
        List<Review> reviews = reviewReader.getWalkwayReviews(size, review, walkway, sort);

        return ReviewMapper.toGetWalkwayReviewsResponse(reviews, size);
    }

    @Transactional(readOnly = true)
    public GetWalkwayRatingResponse getWalkwayRating(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        if (walkway.getExposeLevel().equals(ExposeLevel.PRIVATE) && !walkway.getMember().getId().equals(memberId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_PRIVATE);
        }

        List<RatingCount> ratingCounts = reviewReader.getWalkwaysRating(walkwayId);

        return ReviewMapper.toGetWalkwayRatingResponse(ratingCounts, walkway);
    }
}
