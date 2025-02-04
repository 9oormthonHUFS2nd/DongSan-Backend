package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.review.ReviewWriter;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewRepository;
import com.dongsan.domains.walkway.entity.Walkway;
import fixture.MemberFixture;
import fixture.ReviewFixture;
import fixture.WalkwayFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewCommandService Unit Test")
class ReviewWriterTest {

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewWriter reviewWriter;

    @Nested
    @DisplayName("createReview 메서드는")
    class Describe_createReview {
        @Test
        @DisplayName("리뷰 생성에 성공하면 리뷰를 반환한다.")
        void it_returns_Review() {
            // Given
            Long reviewId = 1L;
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            Review review = ReviewFixture.createReviewWithId(reviewId, member, walkway);

            when(reviewRepository.save(any())).thenReturn(review);

            // When
            Review result = reviewWriter.createReview(review);

            // Then
            assertThat(result.getId()).isEqualTo(reviewId);
        }
    }
}