package com.dongsan.domains.review.repository;

import static fixture.MemberFixture.createMember;
import static fixture.ReviewFixture.createReview;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DisplayName("ReviewQueryDSLRepository Unit Test")
class ReviewQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    ReviewQueryDSLRepository reviewQueryDSLRepository;

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{
        Member member;
        Walkway walkway;
        List<Review> reviews = new ArrayList<>();

        @BeforeEach
        void setUp(){
            member = createMember();
            walkway = createWalkway(member);
            em.persist(member);
            em.persist(walkway);
            for(int i =0; i<6; i++){
                Review review = createReview(member, walkway);
                reviews.add(review);
                em.persist(review);
            }
        }

        @Test
        @DisplayName("reviewId가 null이면 가장 최근의 review들을 내림차순으로 가져온다.")
        void it_returns_most_recent_reviews(){
            // given
            Integer limit = 5;
            Long reviewId = null;
            Long memberId = member.getId();

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
        @DisplayName("reviewId가 null이 아니면 reviewId 보다 일찍 작성한 리뷰(id가 더 작음)를 reviewId 내림차순으로 가져온다.")
        void it_returns_next_reviews(){
            // given
            Integer limit = 5;
            Long reviewId = reviews.get(2).getId();
            Long memberId = member.getId();

            // when
            List<Review> result = reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result.size()).isEqualTo(2);
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
            Long reviewId = reviews.get(5).getId();
            Long memberId = member.getId() + 1;

            // when
            List<Review> result = reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getWalkwayReviewsLatest 메서드는")
    class Describe_getWalkwayReviewsLatest {
        Member member;
        Walkway walkway;
        List<Review> reviews = new ArrayList<>();
        @BeforeEach
        void setUp(){
            member = createMember();
            walkway = createWalkway(member);
            em.persist(member);
            em.persist(walkway);
            for(int i =0; i<6; i++){
                Review review = createReview(member, walkway);
                reviews.add(review);
                em.persist(review);
            }
        }

        @Test
        @DisplayName("리뷰 리스트를 시간순으로 반환한다.")
        void it_returns_review_list_latest() {
            // Given
            Integer limit = 5;
            Long walkwayId = walkway.getId();

            // When
            List<Review> result = reviewQueryDSLRepository.getWalkwayReviewsLatest(limit, null, walkwayId);

            // Then
            Long beforeId = result.get(0).getId();
            for(int count = 1; count < 5; count++) {
                Long currentId = result.get(count).getId();
                assertThat(currentId).isLessThan(beforeId);
                beforeId = currentId;
            }
        }
    }

    @Nested
    @DisplayName("getWalkwayReviewsRating 메서드는")
    class Describe_getWalkwayReviewsRating {
        Member member;
        Walkway walkway;
        List<Review> reviews = new ArrayList<>();
        @BeforeEach
        void setUp(){
            member = createMember();
            walkway = createWalkway(member);
            em.persist(member);
            em.persist(walkway);
            for(Byte i = 0; i<6; i++){
                Review review = createReview(member, walkway, i, "test");
                reviews.add(review);
                em.persist(review);
            }
        }

        @Test
        @DisplayName("리뷰 리스트를 별점순으로 반환한다.")
        void it_returns_review_list_rating() {
            // Given
            Integer limit = 5;
            Long walkwayId = walkway.getId();
            Byte rating = 5;

            // When
            List<Review> result = reviewQueryDSLRepository.getWalkwayReviewsRating(limit, null, walkwayId, rating);

            // Then
            Byte beforeRating = result.get(0).getRating();
            for(int count = 1; count < 5; count++) {
                Byte currentRating = result.get(count).getRating();
                assertThat(currentRating).isLessThan(beforeRating);
                beforeRating = currentRating;
            }
        }
    }

    @Nested
    @DisplayName("getWalkwaysRating 메서드는")
    class Describe_getWalkwaysRating {
        Member member;
        Walkway walkway;
        List<Review> reviews = new ArrayList<>();
        @BeforeEach
        void setUp(){
            member = createMember();
            walkway = createWalkway(member);
            em.persist(member);
            em.persist(walkway);
            for(Byte i = 1; i<=5; i++){
                Review review = createReview(member, walkway, i, "test");
                reviews.add(review);
                em.persist(review);
            }
        }

        @Test
        @DisplayName("각 별점의 개수를 저장한 튜플 리스트를 반환한다.")
        void it_returns_rating_tuple_list() {
            // When
            List<RatingCount> result = reviewQueryDSLRepository.getWalkwaysRating(walkway.getId());

            // Then
            for(RatingCount ratingCount : result) {
                assertThat(ratingCount.count()).isEqualTo(1);
            }
        }
    }

}