//package com.dongsan.domains.user.usecase;
//
//import static fixture.ReflectFixture.reflectCreatedAt;
//import static fixture.ReflectFixture.reflectField;
//import static fixture.WalkwayFixture.createWalkway;
//import static fixture.WalkwayFixture.createWalkwayWithId;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.UserWalkwayUseCase;
//import com.dongsan.core.domains.walkway.WalkwayListResponse;
//import com.dongsan.core.domains.walkway.WalkwayListResponse.WalkwayResponse;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.walkway.service.LikedWalkwayReader;
//import com.dongsan.core.domains.walkway.WalkwayReader;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.IntStream;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("UserWalkwayUseCase Unit Test")
//class UserWalkwayUseCaseTest {
//    @InjectMocks
//    UserWalkwayUseCase userWalkwayUseCase;
//    @Mock
//    WalkwayReader walkwayQueryService;
//    @Mock
//    LikedWalkwayReader likedWalkwayQueryService;
//
//    @Nested
//    @DisplayName("getUserUploadWalkway 메소드는")
//    class Describe_getUserUploadWalkway{
//        @Test
//        @DisplayName("산책로가 존재하면 산책로를 변환한다.")
//        void it_returns_walkway_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = 6L;
//            List<Walkway> walkways = IntStream.range(0, 5)
//                    .mapToObj(index ->
//                            createWalkwayWithId((long)(index+1), null)
//                    ).toList();
//            Walkway walkway = createWalkway(null);
//            reflectField(walkway, "id", walkwayId);
//            LocalDateTime lastCreated = LocalDateTime.of(2024, 12, 23, 11, 11);
//            reflectCreatedAt(walkway, lastCreated);
//
//            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
//            when(walkwayQueryService.getUserWalkWay(memberId, size+1, lastCreated)).thenReturn(walkways);
//
//            // when
//            WalkwayListResponse response = userWalkwayUseCase.getUserUploadWalkway(memberId, size, walkwayId);
//
//            // then
//            Assertions.assertThat(response.walkways()).hasSameSizeAs(walkways);
//            for(int i=0; i<response.walkways().size(); i++){
//                WalkwayResponse walkwayResponse = response.walkways().get(i);
//                Walkway walkwayInfo = walkways.get(i);
//                Assertions.assertThat(walkwayResponse.walkwayId()).isEqualTo(walkwayInfo.getId());
//                Assertions.assertThat(walkwayResponse.name()).isEqualTo(walkwayInfo.getName());
//                Assertions.assertThat(walkwayResponse.hashtags()).hasSameSizeAs(walkwayInfo.getHashtagWalkways());
//            }
//        }
//
//        @Test
//        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
//        void it_returns_empty_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = null;
//            LocalDateTime lastCreatedAt = null;
//            List<Walkway> walkways = Collections.emptyList();
//
//            when(walkwayQueryService.getUserWalkWay(memberId, size+1, lastCreatedAt)).thenReturn(walkways);
//
//            // when
//            WalkwayListResponse response = userWalkwayUseCase.getUserUploadWalkway(memberId, size, walkwayId);
//
//            // then
//            Assertions.assertThat(response.walkways()).isEmpty();
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getUserLikedWalkway 메서드는")
//    class Describe_getUserLikedWalkway{
//        @Test
//        @DisplayName("산책로가 존재하면 산책로 리스트로 변환한다.")
//        void it_returns_walkway_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = 6L;
//            List<Walkway> walkways = IntStream.range(0, 5)
//                    .mapToObj(index -> createWalkwayWithId((long) (index + 1), null))
//                    .toList();
//
//            Walkway walkway = createWalkway(null);
//            reflectField(walkway, "id", walkwayId);
//
//            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
//            when(likedWalkwayQueryService.getUserLikedWalkway(memberId, size+1, walkwayId)).thenReturn(walkways);
//
//            // when
//            WalkwayListResponse response = userWalkwayUseCase.getUserLikedWalkway(memberId, size, walkwayId);
//
//            // then
//            Assertions.assertThat(response.walkways()).hasSameSizeAs(walkways);
//            for(int i=0; i<response.walkways().size(); i++){
//                WalkwayResponse walkwayResponse = response.walkways().get(i);
//                Walkway walkwayInfo = walkways.get(i);
//                Assertions.assertThat(walkwayResponse.walkwayId()).isEqualTo(walkwayInfo.getId());
//                Assertions.assertThat(walkwayResponse.name()).isEqualTo(walkwayInfo.getName());
//                Assertions.assertThat(walkwayResponse.hashtags()).hasSameSizeAs(walkwayInfo.getHashtagWalkways());
//            }
//        }
//
//        @Test
//        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
//        void it_returns_empty_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            Long walkwayId = null;
//            List<Walkway> walkways = Collections.emptyList();
//            when(likedWalkwayQueryService.getUserLikedWalkway(memberId, size+1, walkwayId)).thenReturn(walkways);
//
//            // when
//            WalkwayListResponse response = userWalkwayUseCase.getUserLikedWalkway(memberId, size, walkwayId);
//
//            // then
//            Assertions.assertThat(response.walkways()).isEmpty();
//        }
//    }
//}