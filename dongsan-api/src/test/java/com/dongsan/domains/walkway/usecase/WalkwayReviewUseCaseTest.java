package com.dongsan.domains.walkway.usecase;

import static fixture.ReviewFixture.createReviewWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.service.ReviewCommandService;
import com.dongsan.domains.review.service.ReviewQueryService;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.exception.CustomException;
import fixture.MemberFixture;
import fixture.ReviewFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayReviewUseCase Unit Test")
class WalkwayReviewUseCaseTest {
    @Mock
    WalkwayQueryService walkwayQueryService;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    ReviewCommandService reviewCommandService;
    @Mock
    ReviewQueryService reviewQueryService;
    @InjectMocks
    WalkwayReviewUseCase walkwayReviewUseCase;

    @Nested
    @DisplayName("createReview 메서드는")
    class Describe_createReview {
        @Test
        @DisplayName("리뷰를 등록하고 DTO를 반환한다.")
        void it_returns_responseDTO() {
            // Given
            Long memberId = 1L;
            Long walkwayId = 1L;
            Long reviewId = 1L;
            Byte rating = 5;
            CreateReviewRequest createReviewRequest = new CreateReviewRequest(rating, "test content");

            Member member = MemberFixture.createMemberWithId(memberId);
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, member);
            Review review = ReviewFixture.createReviewWithId(reviewId, member, walkway);

            when(memberQueryService.readMember(memberId)).thenReturn(Optional.of(member));
            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(Optional.of(walkway));
            when(reviewCommandService.createReview(any())).thenReturn(review);

            // When
            CreateReviewResponse result = walkwayReviewUseCase.createReview(memberId, walkwayId, createReviewRequest);

            // Then
            assertThat(result.reviewId()).isEqualTo(reviewId);
        }

        @Test
        @DisplayName("회원이 존재하지 않으면 예외처리한다.")
        void it_returns_exception_MEMBER_NOT_FOUND() {
            // Given
            Long memberId = 1L;
            Long walkwayId = 1L;
            Byte rating = 5;
            CreateReviewRequest createReviewRequest = new CreateReviewRequest(rating, "test content");

            when(memberQueryService.readMember(memberId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> walkwayReviewUseCase.createReview(memberId, walkwayId, createReviewRequest))
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("getWalkwayReviews 메서드는")
    class Describe_getWalkwayReviews {
        @Test
        @DisplayName("type이 rating이면 리뷰를 별점순으로 반환한다.")
        void it_returns_review_list_rating() {
            // Given
            Integer size = 5;
            Long walkwayId = 1L;
            Byte rating = 5;
            String type = "rating";
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(createReviewWithId(1L, member, walkway));
            }

            when(reviewQueryService.getWalkwayReviewsRating(size, null, walkwayId, rating)).thenReturn(reviews);

            // When
            GetWalkwayReviewsResponse result = walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, rating, size);

            // Then
            assertThat(result.reviews().size()).isEqualTo(size);
        }

        @Test
        @DisplayName("type이 latest이면 리뷰를 시간으로 반환한다.")
        void it_returns_review_list_latest() {
            // Given
            Integer size = 5;
            Long walkwayId = 1L;
            Byte rating = 5;
            String type = "latest";
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(createReviewWithId(1L, member, walkway));
            }

            when(reviewQueryService.getWalkwayReviewsLatest(size, null, walkwayId)).thenReturn(reviews);

            // When
            GetWalkwayReviewsResponse result = walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, rating, size);

            // Then
            assertThat(result.reviews().size()).isEqualTo(size);
        }

        @Test
        @DisplayName("type이 latest나 rating이 아니면 예외처리한다.")
        void it_returns_exception() {
            // Given
            Integer size = 5;
            Long walkwayId = 1L;
            Byte rating = 5;
            String type = "크아아아ㅏ아아아악";

            // When & Then
            assertThatThrownBy(() -> walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, rating, size))
                    .isInstanceOf(CustomException.class);
        }
    }
}