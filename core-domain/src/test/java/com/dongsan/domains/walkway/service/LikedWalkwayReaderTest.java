//package com.dongsan.domains.walkway.service;
//
//import static fixture.LikedWalkwayFixture.createLikedWalkway;
//import static fixture.ReflectFixture.reflectCreatedAt;
//import static fixture.ReflectFixture.reflectField;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.service.LikedWalkwayReader;
//import com.dongsan.common.error.code.LikedWalkwayErrorCode;
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.domains.walkway.entity.LikedWalkway;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.domains.walkway.repository.LikedWalkwayQueryDSLRepository;
//import com.dongsan.domains.walkway.repository.LikedWalkwayRepository;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("LikedWalkwayQueryService Unit Test")
//class LikedWalkwayReaderTest {
//    @InjectMocks
//    LikedWalkwayReader likedWalkwayQueryService;
//    @Mock
//    LikedWalkwayRepository likedWalkwayRepository;
//    @Mock
//    LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getUserLikedWalkway 메서드는")
//    class Describe_getUserLikedWalkway{
//        @Test
//        @DisplayName("walkway가 null이면 첫 페이지의 LikedWalkway들을 반환한다.")
//        void it_returns_first_page_likedWalkways(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = null;
//            List<LikedWalkway> likedWalkways = IntStream.range(0, 5)
//                    .mapToObj(index -> createLikedWalkway(null, createWalkway(null)))
//                    .toList();
//            when(likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, null)).thenReturn(likedWalkways);
//
//            // when
//            List<Walkway> result = likedWalkwayQueryService.getUserLikedWalkway(memberId, size, walkwayId);
//
//            // then
//            assertThat(result)
//                    .hasSameSizeAs(likedWalkways)
//                    .isEqualTo(likedWalkways.stream().map(LikedWalkway::getWalkway).toList());
//        }
//
//        @Test
//        @DisplayName("walkway가 null이 아니면 다음 페이지의 LikedWalkway들을 반환한다.")
//        void it_returns_next_page_likedWalkways(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = 6L;
//            LikedWalkway likedWalkway = createLikedWalkway(null, null);
//            reflectField(likedWalkway, "id", 1L);
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 23, 11, 11);
//            reflectCreatedAt(likedWalkway, lastCreatedAt);
//            List<LikedWalkway> likedWalkways = IntStream.range(0, 5)
//                    .mapToObj(index -> createLikedWalkway(null, createWalkway(null)))
//                    .toList();
//            when(likedWalkwayRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(Optional.of(likedWalkway));
//            when(likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkway.getCreatedAt())).thenReturn(likedWalkways);
//
//            // when
//            List<Walkway> result = likedWalkwayQueryService.getUserLikedWalkway(memberId, size, walkwayId);
//
//            // then
//            assertThat(result)
//                    .hasSameSizeAs(likedWalkways)
//                    .isEqualTo(likedWalkways.stream().map(LikedWalkway::getWalkway).toList());
//        }
//    }
//
//    @Nested
//    @DisplayName("findByMemberIdAndWalkwayId 메서드는")
//    class Describe_findByMemberIdAndWalkwayId{
//        @Test
//        @DisplayName("LikedWalkway가 존재하면 LikedWalkway를 반환한다.")
//        void it_returns_likedWalkway(){
//            // given
//            Long memberId = 1L;
//            Long walkwayId = 1L;
//            LikedWalkway likedWalkway = createLikedWalkway(null, null);
//            when(likedWalkwayRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(Optional.of(likedWalkway));
//
//            // when
//            LikedWalkway result = likedWalkwayQueryService.findByMemberIdAndWalkwayId(memberId, walkwayId);
//
//            // then
//            assertThat(result).isSameAs(likedWalkway);
//        }
//
//        @Test
//        @DisplayName("LikedWalkway가 존재하지 않으면 예외를 반환한다.")
//        void it_throws_exception(){
//            // given
//            Long memberId = 1L;
//            Long walkwayId = 1L;
//            when(likedWalkwayRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(Optional.empty());
//
//            // when & then
//            CustomException thrown = assertThrows(CustomException.class, () -> {
//                likedWalkwayQueryService.findByMemberIdAndWalkwayId(memberId, walkwayId);
//            });
//            assertEquals(LikedWalkwayErrorCode.LIKED_WALKWAY_NOT_FOUND, thrown.getErrorCode());
//        }
//    }
//
//    @Nested
//    @DisplayName("existByMemberIdAndWalkwayId 메서드는")
//    class Describe_existByMemberIdAndWalkwayId {
//        @Test
//        @DisplayName("MemberId와 WalkwayId로 LikedWalkway의 유무를 반환한다.")
//        void it_returns_exists() {
//            // Given
//            Long memberId = 1L;
//            Long walkwayId = 1L;
//
//            when(likedWalkwayRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(true);
//
//            // When
//            Boolean result = likedWalkwayQueryService.existsByMemberIdAndWalkwayId(memberId, walkwayId);
//
//            // Then
//            assertThat(result).isTrue();
//        }
//    }
//}