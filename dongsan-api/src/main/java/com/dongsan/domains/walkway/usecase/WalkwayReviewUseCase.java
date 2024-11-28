package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.service.ReviewCommandService;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.ReviewMapper;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.code.MemberErrorCode;
import com.dongsan.error.code.WalkwayErrorCode;
import com.dongsan.error.exception.CustomException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class WalkwayReviewUseCase {

    private final ReviewCommandService reviewCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;

    public CreateReviewResponse createReview(Long memberId, Long walkwayId, CreateReviewRequest createReviewRequest) {
        Member member = memberQueryService.readMember(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Walkway walkway = walkwayQueryService.getWalkway(walkwayId)
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND));

        Review review = ReviewMapper.toReview(createReviewRequest, walkway, member);

        review = reviewCommandService.createReview(review);

        return ReviewMapper.toCreateReviewResponse(review);
    }
}
