//package com.dongsan.domains.walkway.service.factory;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.core.domains.walkway.WalkwaySort;
//import com.dongsan.core.domains.walkway.service.factory.SearchWalkwayFactory;
//import com.dongsan.core.domains.walkway.service.search.walkway.SearchWalkwayLiked;
//import com.dongsan.core.domains.walkway.service.search.walkway.SearchWalkwayRating;
//import com.dongsan.core.domains.walkway.service.search.walkway.SearchWalkway;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("SearchWalkwayServiceFactory Unit Test")
//class SearchWalkwayFactoryTest {
//
//    @Mock
//    private SearchWalkwayLiked searchLikedWalkwayService;
//    @Mock
//    private SearchWalkwayRating searchRatingWalkwayService;
//    private SearchWalkwayFactory factory;
//
//    @BeforeEach
//    void setUp() {
//        // Mock 서비스의 Sort 반환값 설정
//        Mockito.when(searchLikedWalkwayService.getSortType()).thenReturn(WalkwaySort.LIKED);
//        Mockito.when(searchRatingWalkwayService.getSortType()).thenReturn(WalkwaySort.RATING);
//
//        // 팩토리에 서비스 리스트 주입
//        factory = new SearchWalkwayFactory(List.of(searchLikedWalkwayService, searchRatingWalkwayService));
//    }
//
//    @Nested
//    @DisplayName("getService 메서드는")
//    class Describe_getService {
//        @Test
//        @DisplayName("LIKED가 입력 되면 해당되는 서비스를 반환한다.")
//        void it_returns_liked_service() {
//            SearchWalkway service = factory.getService(WalkwaySort.LIKED);
//            assertThat(service).isEqualTo(searchLikedWalkwayService);
//        }
//
//        @Test
//        @DisplayName("Rating이 입력 되면 해당되는 서비스를 반환한다.")
//        void it_returns_rating_service() {
//            SearchWalkway service = factory.getService(WalkwaySort.RATING);
//            assertThat(service).isEqualTo(searchRatingWalkwayService);
//        }
//    }
//}