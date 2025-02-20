//package com.dongsan.domains.walkway.service;
//
//import static fixture.ReviewFixture.createReview;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.review.ReviewReader;
//import com.dongsan.domains.review.entity.Review;
//import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
//import com.dongsan.domains.review.repository.ReviewRepository;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.review.ReviewSort;
//import com.dongsan.core.domains.walkway.service.factory.GetReviewsServiceFactory;
//import com.dongsan.core.domains.review.GetReviewsService;
//import fixture.WalkwayFixture;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ReviewQueryServiceTest Unit Test")
//class ReviewReaderTest {
//    @InjectMocks
//    private ReviewReader reviewReader;
//    @Mock
//    private ReviewQueryDSLRepository reviewQueryDSLRepository;
//    @Mock
//    private ReviewRepository reviewRepository;
//    @Mock
//    private GetReviewsServiceFactory getReviewsServiceFactory;
//    @Mock
//    private GetReviewsService getReviewsService;
//
//    @Nested
//    @DisplayName("getReviews 메소드는")
//    class Describe_getReviews{
//        @Test
//        @DisplayName("리뷰가 존재하면 리뷰 목록을 반환한다.")
//        void it_returns_reviews(){
//            // given
//            Integer limit = 5;
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 23, 11, 11);
//            Long memberId = 1L;
//
//            List<Review> reviews = new ArrayList<>();
//            for(int i =0; i<5; i++){
//                reviews.add(createReview(null, null));
//            }
//
//            when(reviewQueryDSLRepository.getUserReviews(limit, lastCreatedAt, memberId)).thenReturn(reviews);
//
//            // when
//            List<Review> result = reviewReader.getReviews(limit, lastCreatedAt, memberId);
//
//            // then
//            assertThat(result).hasSize(5);
//            verify(reviewQueryDSLRepository).getUserReviews(limit, lastCreatedAt, memberId);
//        }
//
//        @Test
//        @DisplayName("lastCreatedAt가 존재하지 않으면 비어 있는 리뷰 목록을 반환한다.")
//        void it_returns_empty_review(){
//            // given
//            Integer limit = 5;
//            LocalDateTime lastCreatedAt = null;
//            Long memberId = 1L;
//
//            when(reviewQueryDSLRepository.getUserReviews(limit, lastCreatedAt, memberId)).thenReturn(Collections.emptyList());
//
//            // when
//            List<Review> result = reviewReader.getReviews(limit, lastCreatedAt, memberId);
//
//            // then
//            assertThat(result).isEmpty();
//            verify(reviewQueryDSLRepository).getUserReviews(limit, lastCreatedAt, memberId);
//        }
//    }
//
//    @Nested
//    @DisplayName("existsByReviewId 메소드는")
//    class Describe_existsByReviewId{
//        @Test
//        @DisplayName("리뷰가 존재하면 true를 반환한다.")
//        void it_returns_true_when_review_exists() {
//            // given
//            Long reviewId = 1L;
//            when(reviewRepository.existsById(reviewId)).thenReturn(true);
//
//            // when
//            boolean result = reviewReader.existsByReviewId(reviewId);
//
//            // then
//            assertThat(result).isTrue();
//            verify(reviewRepository).existsById(reviewId);
//        }
//
//        @Test
//        @DisplayName("리뷰가 존재하지 않으면 false를 반환한다.")
//        void it_returns_false_when_review_does_not_exist() {
//            // given
//            Long reviewId = 1L;
//            when(reviewRepository.existsById(reviewId)).thenReturn(false);
//
//            // when
//            boolean result = reviewReader.existsByReviewId(reviewId);
//
//            // then
//            assertThat(result).isFalse();
//            verify(reviewRepository).existsById(reviewId);
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwayReviews 메서드는")
//    class Describe_getWalkwayReviews {
//        @Test
//        @DisplayName("리뷰를 최신순으로 반환한다.")
//        void it_returns_reviews() {
//            // Given
//            Integer size = 5;
//            ReviewSort sort = ReviewSort.RATING;
//            List<Review> reviews = List.of();
//            Walkway walkway = WalkwayFixture.createWalkway(null);
//
//            when(getReviewsServiceFactory.getService(sort)).thenReturn(getReviewsService);
//            when(getReviewsService.search(size, null, walkway)).thenReturn(reviews);
//
//            // When
//            List<Review> result = reviewReader.getWalkwayReviews(size, null, walkway, sort);
//
//            // Then
//            assertThat(result).isEqualTo(reviews);
//        }
//    }
//
//}