//package com.dongsan.domains.walkway.service.factory;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.review.ReviewSort;
//import com.dongsan.core.domains.walkway.service.factory.GetReviewsServiceFactory;
//import com.dongsan.core.domains.review.GetLatestReviewsService;
//import com.dongsan.core.domains.review.GetRatingReviewsService;
//import com.dongsan.core.domains.review.GetReviewsService;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("GetReviewsServiceFactory Unit Test")
//class GetReviewsServiceFactoryTest {
//
//    @Mock
//    private GetLatestReviewsService getLatestReviewsService;
//    @Mock
//    private GetRatingReviewsService getRatingReviewsService;
//    private GetReviewsServiceFactory factory;
//
//    @BeforeEach
//    void setUp() {
//        // Mock 서비스의 Sort 반환값 설정
//        when(getLatestReviewsService.getSortType()).thenReturn(ReviewSort.LATEST);
//        when(getRatingReviewsService.getSortType()).thenReturn(ReviewSort.RATING);
//
//        // 팩토리에 서비스 리스트 주입
//        factory = new GetReviewsServiceFactory(List.of(getLatestReviewsService, getRatingReviewsService));
//    }
//
//    @Nested
//    @DisplayName("getService 메서드는")
//    class Describe_getService {
//        @Test
//        @DisplayName("Latest가 입력 되면 해당 되는 서비스를 반환한다.")
//        void it_returns_latest_service() {
//            GetReviewsService service = factory.getService(ReviewSort.LATEST);
//            assertThat(service).isEqualTo(getLatestReviewsService);
//        }
//
//        @Test
//        @DisplayName("Rating이 입력 되면 해당 되는 서비스를 반환한다.")
//        void it_returns_rating_service() {
//            GetReviewsService service = factory.getService(ReviewSort.RATING);
//            assertThat(service).isEqualTo(getRatingReviewsService);
//        }
//    }
//}