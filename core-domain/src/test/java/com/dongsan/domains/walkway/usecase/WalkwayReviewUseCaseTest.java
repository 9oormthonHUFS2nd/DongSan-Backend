package com.dongsan.domains.walkway.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.dongsan.domains.walkway.enums.ReviewSort;
import com.dongsan.domains.walkway.service.ReviewCommandService;
import com.dongsan.domains.walkway.service.ReviewQueryService;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.MemberFixture;
import fixture.ReviewFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.List;
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
    @Mock
    WalkwayCommandService walkwayCommandService;
    @Mock
    WalkwayHistoryQueryService walkwayHistoryQueryService;
    @Mock
    WalkwayHistoryCommandService walkwayHistoryCommandService;
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
            Integer rating = 5;
            CreateReviewRequest createReviewRequest = new CreateReviewRequest(rating, "test content");

            Member member = MemberFixture.createMemberWithId(memberId);
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, member);
            Review review = ReviewFixture.createReviewWithId(reviewId, member, walkway);
            WalkwayHistory walkwayHistory = WalkwayHistory.builder()
                    .walkway(walkway)
                    .member(member)
                    .time(10)
                    .distance(10.0)
                    .build();

            when(memberQueryService.getMember(member.getId())).thenReturn(member);
            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
            when(walkwayHistoryQueryService.findByWalkwayAndMember(walkwayId, memberId)).thenReturn(walkwayHistory);
            when(reviewCommandService.createReview(any())).thenReturn(review);

            // When
            CreateReviewResponse result = walkwayReviewUseCase.createReview(memberId, walkwayId, createReviewRequest);

            // Then
            assertThat(result.reviewId()).isEqualTo(reviewId);
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
            String type = "rating";
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            ReviewSort sort = ReviewSort.RATING;

            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(ReviewFixture.createReviewWithId(1L, member, walkway));
            }

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(reviewQueryService.getWalkwayReviews(size, null, walkway, sort)).thenReturn(reviews);

            // When
            GetWalkwayReviewsResponse result = walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, size, member.getId());

            // Then
            assertThat(result.reviews()).hasSize(size);
        }

        @Test
        @DisplayName("type이 latest이면 리뷰를 시간으로 반환한다.")
        void it_returns_review_list_latest() {
            // Given
            Integer size = 5;
            Long walkwayId = 1L;
            String type = "latest";
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            ReviewSort sort = ReviewSort.LATEST;

            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(ReviewFixture.createReviewWithId(1L, member, walkway));
            }

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(reviewQueryService.getWalkwayReviews(size, null, walkway, sort)).thenReturn(reviews);

            // When
            GetWalkwayReviewsResponse result = walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, size, member.getId());

            // Then
            assertThat(result.reviews()).hasSize(size);
        }

        @Test
        @DisplayName("type이 latest나 rating이 아니면 예외처리한다.")
        void it_returns_exception() {
            // Given
            Integer size = 5;
            Long walkwayId = 1L;
            String type = "크아아아ㅏ아아아악";
            Member member = MemberFixture.createMember();

            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, null);

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);

            // When & Then
            assertThatThrownBy(() -> walkwayReviewUseCase.getWalkwayReviews(type, null, walkwayId, size, member.getId()))
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("getWalkwayRating 메서드는")
    class Describe_getWalkwayRating {
        @Test
        @DisplayName("산책로가 존재하면 산책로의 별점 내용을 반환한다.")
        void it_returns_walkway_rating_info() {
            // Given
            Walkway walkway = WalkwayFixture.createWalkway(null);
            Member member = MemberFixture.createMember();

            List<RatingCount> ratingCounts = new ArrayList<>();
            for(Integer i = 1; i <= 5; i++) {
                ratingCounts.add(new RatingCount(i, 10L));
            }

            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
            when(reviewQueryService.getWalkwaysRating(walkway.getId())).thenReturn(ratingCounts);

            // When
            GetWalkwayRatingResponse result = walkwayReviewUseCase.getWalkwayRating(walkway.getId(), member.getId());

            // Then
            assertThat(result.five()).isEqualTo(20L);
            assertThat(result.four()).isEqualTo(20L);
            assertThat(result.three()).isEqualTo(20L);
            assertThat(result.two()).isEqualTo(20L);
            assertThat(result.one()).isEqualTo(20L);
        }

        @Test
        @DisplayName("산책로가 존재하고 리뷰가 없으면 빈 별점 내용을 반환한다.")
        void it_returns_walkway_rating_empty_info() {
            // Given
            Walkway walkway = WalkwayFixture.createWalkway(null);
            Member member = MemberFixture.createMember();

            List<RatingCount> ratingCounts = new ArrayList<>();

            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
            when(reviewQueryService.getWalkwaysRating(walkway.getId())).thenReturn(ratingCounts);

            // When
            GetWalkwayRatingResponse result = walkwayReviewUseCase.getWalkwayRating(walkway.getId(), member.getId());

            // Then
            assertThat(result.five()).isZero();
            assertThat(result.four()).isZero();
            assertThat(result.three()).isZero();
            assertThat(result.two()).isZero();
            assertThat(result.one()).isZero();
        }
    }
}