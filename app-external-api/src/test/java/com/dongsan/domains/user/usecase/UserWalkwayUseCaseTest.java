package com.dongsan.domains.user.usecase;

import static fixture.LikedWalkwayFixture.createLikedWalkway;
import static fixture.ReflectFixture.reflectCreatedAt;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static fixture.WalkwayFixture.createWalkwayWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.LikedWalkwayQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.time.LocalDateTime;
import java.util.Collections;
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
@DisplayName("UserWalkwayUseCase Unit Test")
class UserWalkwayUseCaseTest {
    @InjectMocks
    UserWalkwayUseCase userWalkwayUseCase;
    @Mock
    WalkwayQueryService walkwayQueryService;
    @Mock
    LikedWalkwayQueryService likedWalkwayQueryService;

    @Nested
    @DisplayName("getUserUploadWalkway 메소드는")
    class Describe_getUserUploadWalkway{
        @Test
        @DisplayName("산책로가 존재하면 산책로를 변환한다.")
        void it_returns_walkway_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 6L;
            List<Walkway> walkways = IntStream.range(0, 5)
                    .mapToObj(index ->
                            createWalkwayWithId((long)(index+1), null)
                    ).toList();

            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", walkwayId);
            LocalDateTime lastCreated = LocalDateTime.of(2024, 12, 23, 11, 11);
            reflectCreatedAt(walkway, lastCreated);

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(walkwayQueryService.getUserWalkWay(memberId, size, lastCreated)).thenReturn(walkways);

            // when
            List<Walkway> response = userWalkwayUseCase.getUserUploadWalkway(memberId, size, walkwayId);

            // then
            assertThat(response).hasSameSizeAs(walkways);
            for(int i=0; i<response.size(); i++){
                Walkway walkwayResponse = response.get(i);
                Walkway walkwayInfo = walkways.get(i);
                assertThat(walkwayResponse.getId()).isEqualTo(walkwayInfo.getId());
                assertThat(walkwayResponse.getName()).isEqualTo(walkwayInfo.getName());
                assertThat(walkwayResponse.getHashtagWalkways()).hasSameSizeAs(walkwayInfo.getHashtagWalkways());
            }
        }

        @Test
        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = null;
            LocalDateTime lastCreatedAt = null;
            List<Walkway> walkways = Collections.emptyList();

            when(walkwayQueryService.getUserWalkWay(memberId, size, lastCreatedAt)).thenReturn(walkways);

            // when
            List<Walkway> response = userWalkwayUseCase.getUserUploadWalkway(memberId, size, walkwayId);

            // then
            assertThat(response).isEmpty();
        }

    }

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway{
        @Test
        @DisplayName("산책로가 존재하면 산책로 리스트로 변환한다.")
        void it_returns_walkway_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 6L;
            List<LikedWalkway> likedWalkways = IntStream.range(0, 5)
                    .mapToObj(index -> {
                            Walkway walkway = createWalkwayWithId((long) (index + 1), null);
                            return createLikedWalkway(null, walkway);
                        })
                    .toList();

            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", walkwayId);

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(likedWalkwayQueryService.getUserLikedWalkway(memberId, size, walkwayId)).thenReturn(likedWalkways);

            // when
            List<Walkway> response = userWalkwayUseCase.getUserLikedWalkway(memberId, size, walkwayId);

            // then
            assertThat(response).hasSameSizeAs(likedWalkways);
            for(int i=0; i<response.size(); i++){
                Walkway walkwayResponse = response.get(i);
                Walkway walkwayInfo = likedWalkways.get(i).getWalkway();
                assertThat(walkwayResponse.getId()).isEqualTo(walkwayInfo.getId());
                assertThat(walkwayResponse.getName()).isEqualTo(walkwayInfo.getName());
                assertThat(walkwayResponse.getHashtagWalkways()).hasSameSizeAs(walkwayInfo.getHashtagWalkways());
            }
        }

        @Test
        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = null;
            List<LikedWalkway> likedWalkways = Collections.emptyList();
            when(likedWalkwayQueryService.getUserLikedWalkway(memberId, size, walkwayId)).thenReturn(likedWalkways);

            // when
            List<Walkway> response = userWalkwayUseCase.getUserLikedWalkway(memberId, size, walkwayId);

            // then
            assertThat(response).isEmpty();
        }
    }
}