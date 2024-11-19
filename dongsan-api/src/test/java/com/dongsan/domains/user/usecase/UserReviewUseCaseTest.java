package com.dongsan.domains.user.usecase;

import static fixture.MemberFixture.createMemberWithId;
import static fixture.ReviewFixture.createReviewWithId;
import static fixture.WalkwayFixture.createWalkwayWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.service.ReviewQueryService;
import com.dongsan.domains.user.dto.response.GetReviewResponse;
import com.dongsan.domains.user.dto.response.GetReviewResponse.ReviewInfo;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserReviewUseCaseTest Unit Test")
class UserReviewUseCaseTest {
    @InjectMocks
    UserReviewUseCase userReviewUseCase;
    @Mock
    ReviewQueryService reviewQueryService;

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{
        @Test
        @DisplayName("리뷰가 존재하면 리뷰를 DTO로 변환한다.")
        void it_returns_responseDTO(){
            // given
            Integer limit = 5;
            Long reviewId = 6L;
            Long memberId = 1L;

            Member member = createMemberWithId(memberId);
            Walkway walkway = createWalkwayWithId(1L, member);
            List<Review> reviews = IntStream.range(0, 5)
                    .mapToObj(index ->
                            createReviewWithId((long) (index+1), member, walkway)
                    ).toList();
            when(reviewQueryService.getReviews(limit, reviewId, memberId)).thenReturn(reviews);

            // when
            GetReviewResponse result = userReviewUseCase.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result.reviews().size()).isEqualTo(5);
            for(int i=0; i<5; i++){
                ReviewInfo reviewInfo = result.reviews().get(i);
                assertThat(reviewInfo.reviewId()).isEqualTo(reviews.get(i).getId());
                assertThat(reviewInfo.walkwayId()).isEqualTo(reviews.get(i).getWalkway().getId());
            }
        }
    }


}