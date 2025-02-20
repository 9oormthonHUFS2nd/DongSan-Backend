//package com.dongsan.domains.walkway.service.search.walkway;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.service.search.walkway.SearchWalkwayRating;
//import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
//import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.walkway.WalkwaySort;
//import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
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
//@DisplayName("SearchRatingWalkwayService Unit Test")
//class SearchRatingWalkwayServiceTest {
//
//    @Mock
//    private WalkwayQueryDSLRepository walkwayQueryDSLRepository;
//    @InjectMocks
//    private SearchWalkwayRating searchRatingWalkwayService;
//
//    @Nested
//    @DisplayName("getSortType 메서드는")
//    class Describe_getWalkwaySortType {
//        @Test
//        @DisplayName("서비스에 해당하는 정렬을 반환한다.")
//        void it_returns_sort() {
//            assertThat(searchRatingWalkwayService.getSortType()).isEqualTo(WalkwaySort.RATING);
//        }
//    }
//
//    @Nested
//    @DisplayName("search 메서드는")
//    class Describe_search {
//
//        List<SearchWalkwayResult> searchWalkwayResults = new ArrayList<>();
//
//        @BeforeEach
//        void setUp() {
//            for (long i = 1; i <= 10; i++) {
//                SearchWalkwayResult searchWalkwayResult
//                        = new SearchWalkwayResult(i, null, null, null, null, null, null, null, null, null, null);
//                searchWalkwayResults.add(searchWalkwayResult);
//            }
//        }
//
//        @Test
//        @DisplayName("별점순으로 검색한 결과를 반환한다.")
//        void it_returns_rating_result() {
//            // Given
//            Long userId = 1L;
//            Double latitude = 2.0;
//            Double longitude = 2.0;
//            Double distance = 10.0;
//            Walkway lastWalkway = null;
//            int size = 10;
//
//            SearchWalkwayQuery searchWalkwayRequest
//                    = new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkway, size);
//
//            when(walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayRequest)).thenReturn(searchWalkwayResults);
//
//            // When
//            List<SearchWalkwayResult> result = searchRatingWalkwayService.search(searchWalkwayRequest);
//
//            // Then
//            assertThat(result).isNotNull().hasSize(size);
//        }
//    }
//}