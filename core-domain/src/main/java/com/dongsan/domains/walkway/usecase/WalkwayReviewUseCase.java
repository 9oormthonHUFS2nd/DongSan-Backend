package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.service.ReviewCommandService;
import com.dongsan.domains.walkway.service.ReviewQueryService;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayRatingResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.ReviewMapper;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.common.error.code.ReviewErrorCode;
import com.dongsan.common.error.exception.CustomException;
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

    @Transactional
    public CreateReviewResponse createReview(Long memberId, Long walkwayId, CreateReviewRequest createReviewRequest) {
        Member member = memberQueryService.getMember(memberId);

        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        Review review = ReviewMapper.toReview(createReviewRequest, walkway, member);

        review = reviewCommandService.createReview(review);

        walkway.updateRatingAndReviewCount(review.getRating());
        walkwayCommandService.createWalkway(walkway);

        return ReviewMapper.toCreateReviewResponse(review);
    }

    public GetWalkwayReviewsResponse getWalkwayReviews(String type, Long lastId, Long walkwayId, Byte rating, Integer size) {
        if (!walkwayQueryService.existsByWalkwayId(walkwayId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND);
        }

        List<Review> reviews;

        switch (type) {
            case "rating" -> reviews = reviewQueryService.getWalkwayReviewsRating(size, lastId, walkwayId, rating);
            case "latest" -> reviews = reviewQueryService.getWalkwayReviewsLatest(size, lastId, walkwayId);
            default -> throw new CustomException(ReviewErrorCode.INVALID_SORT_TYPE);
        }

        return ReviewMapper.toGetWalkwayReviewsResponse(reviews);
    }

    public GetWalkwayRatingResponse getWalkwayRating(Long walkwayId) {
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        List<RatingCount> ratingCounts = reviewQueryService.getWalkwaysRating(walkwayId);

        return ReviewMapper.toGetWalkwayRatingResponse(ratingCounts, walkway);
    }
}
