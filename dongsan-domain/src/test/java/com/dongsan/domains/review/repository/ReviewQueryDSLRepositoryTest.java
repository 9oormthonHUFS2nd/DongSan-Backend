package com.dongsan.domains.review.repository;

import com.dongsan.common.annotation.RepositoryTest;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static fixture.MemberFixture.createMember;
import static fixture.ReviewFixture.createReview;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ReviewQueryDSLRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewQueryDSLRepository reviewQueryDSLRepository;

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{
        @BeforeEach
        void setUp(){
            Member member = createMember();
            entityManager.persist(member);
            Walkway walkway = createWalkway(member);
            entityManager.persist(walkway);
            for(int i =0; i<6; i++){
                Review review = createReview(member, walkway);
                entityManager.persist(review);
            }
        }

        @Test
        @DisplayName("reviewId가 null이면 첫 Page의 리뷰를 reviewId 내림차순으로 가져온다.")
        void it_returns_first_page_reviews(){
            // given
            Integer limit = 5;
            Long reviewId = null;
            Long memberId = 1L;

            // when
            List<Review> result = reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result.size()).isEqualTo(5);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i - 1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("reviewId가 null이 아니면 reviewId 보다 작은 리뷰를 reviewId 내림차순으로 가져온다.")
        void it_returns_next_reviews(){
            // given
            Integer limit = 5;
            Long reviewId = 4L;
            Long memberId = 1L;

            // when
            List<Review> result = reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result.size()).isEqualTo(3);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i - 1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }

        }

        @Test
        @DisplayName("리뷰가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Integer limit = 5;
            Long reviewId = 5L;
            Long memberId = 2L;

            // when
            List<Review> result = reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result).isEmpty();
        }
    }


}