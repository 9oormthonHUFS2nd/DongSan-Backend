package com.dongsan.domains.review.service;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fixture.ReviewFixture.createReview;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewQueryServiceTest Unit Test")
class ReviewQueryServiceTest {
    @InjectMocks
    private ReviewQueryService reviewQueryService;
    @Mock
    private ReviewQueryDSLRepository reviewQueryDSLRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{
        @Test
        @DisplayName("리뷰 목록을 반환한다.")
        void it_returns_reviews(){
            // given
            Integer limit = 5;
            Long reviewId = 1L;
            Long memberId = 1L;

            List<Review> reviews = new ArrayList<>();
            for(int i =0; i<5; i++){
                reviews.add(createReview(null, null));
            }

            when(reviewQueryDSLRepository.getReviews(limit, reviewId, memberId)).thenReturn(reviews);

            // when
            List<Review> result = reviewQueryService.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result.size()).isEqualTo(5);
            verify(reviewQueryDSLRepository).getReviews(limit, reviewId, memberId);
        }

        @Test
        @DisplayName("비어 있는 리뷰 목록을 반환한다.")
        void it_returns_empty_review(){
            // given
            Integer limit = 5;
            Long reviewId = 1L;
            Long memberId = 1L;

            when(reviewQueryDSLRepository.getReviews(limit, reviewId, memberId)).thenReturn(Collections.emptyList());

            // when
            List<Review> result = reviewQueryService.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result).isEmpty();
            verify(reviewQueryDSLRepository).getReviews(limit, reviewId, memberId);
        }
    }

    @Nested
    @DisplayName("existsByReviewId 메소드는")
    class Describe_existsByReviewId{
        @Test
        @DisplayName("리뷰가 존재하면 true를 반환한다.")
        void it_returns_true_when_review_exists() {
            // given
            Long reviewId = 1L;
            when(reviewRepository.existsById(reviewId)).thenReturn(true);

            // when
            boolean result = reviewQueryService.existsByReviewId(reviewId);

            // then
            assertThat(result).isTrue();
            verify(reviewRepository).existsById(reviewId);
        }

        @Test
        @DisplayName("리뷰가 존재하지 않으면 false를 반환한다.")
        void it_returns_false_when_review_does_not_exist() {
            // given
            Long reviewId = 1L;
            when(reviewRepository.existsById(reviewId)).thenReturn(false);

            // when
            boolean result = reviewQueryService.existsByReviewId(reviewId);

            // then
            assertThat(result).isFalse();
            verify(reviewRepository).existsById(reviewId);
        }
    }

}