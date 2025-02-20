//package com.dongsan.domains.walkway.service.search.review;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.review.GetLatestReviewsService;
//import com.dongsan.domains.review.entity.Review;
//import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.review.ReviewSort;
//import fixture.ReviewFixture;
//import fixture.WalkwayFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("GetLatestReviewsService Unit Test")
//class GetLatestReviewsServiceTest {
//
//    @Mock
//    private ReviewQueryDSLRepository reviewQueryDSLRepository;
//    @InjectMocks
//    private GetLatestReviewsService getLatestReviewsService;
//
//    @Nested
//    @DisplayName("getSortType 메서드는")
//    class Describe_getWalkwaySortType {
//        @Test
//        @DisplayName("서비스에 해당하는 정렬을 반환한다.")
//        void it_returns_sort() {
//            assertThat(getLatestReviewsService.getSortType()).isEqualTo(ReviewSort.LATEST);
//        }
//    }
//
//    @Nested
//    @DisplayName("search 메서드는")
//    class Describe_search {
//
//        List<Review> reviews = new ArrayList<>();
//
//        @BeforeEach
//        void setUp() {
//            for (long i = 1; i <= 10; i++) {
//                Review review = ReviewFixture.createReviewWithId(i, null, null);
//                reviews.add(review);
//            }
//        }
//
//        @Test
//        @DisplayName("좋아요순으로 검색한 결과를 반환한다.")
//        void it_returns_liked_result() {
//            // Given
//            int size = 10;
//            Review review = ReviewFixture.createReview(null, null);
//            Walkway walkway = WalkwayFixture.createWalkway(null);
//
//            when(reviewQueryDSLRepository.getWalkwayReviewsLatest(size, walkway.getId(), review)).thenReturn(reviews);
//
//            // When
//            List<Review> result = getLatestReviewsService.search(size, review, walkway);
//
//            // Then
//            assertThat(result).isNotNull().hasSize(size);
//        }
//    }
//}