package com.dongsan.domains.user.usecase;

import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse.GetWalkwayDetailInfo;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse.UserWalkwaySummaryInfo;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.WalkwayFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static fixture.WalkwayFixture.createWalkwayWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserWalkwayUseCase Unit Test")
class UserWalkwayUseCaseTest {
    @InjectMocks
    UserWalkwayUseCase userWalkwayUseCase;
    @Mock
    WalkwayQueryService walkwayQueryService;

    @Nested
    @DisplayName("getUserWalkwaySummary 메소드는")
    class Describe_getUserWalkwaySummary{
        @Test
        @DisplayName("산책로가 존재하면 DTO로 변환한다.")
        void it_returns_response_DTO(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 6L;
            List<Walkway> walkways = IntStream.range(0, 5)
                    .mapToObj(index ->
                            createWalkwayWithId((long)(index+1), null)
                    ).toList();
            when(walkwayQueryService.getUserWalkWay(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            GetWalkwaySummaryResponse response = userWalkwayUseCase.getUserWalkwaySummary(memberId, size, walkwayId);

            // then
            assertThat(response.walkways().size()).isEqualTo(walkways.size());
            for(int i=0; i<response.walkways().size(); i++){
                UserWalkwaySummaryInfo walkwayInfo = response.walkways().get(i);
                Walkway walkway = walkways.get(i);
                assertThat(walkwayInfo.walkwayId()).isEqualTo(walkway.getId());
                assertThat(walkwayInfo.name()).isEqualTo(walkway.getName());
            }
        }

        @Test
        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 1L;
            List<Walkway> walkways = Collections.emptyList();
            when(walkwayQueryService.getUserWalkWay(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            GetWalkwaySummaryResponse response = userWalkwayUseCase.getUserWalkwaySummary(memberId, size, walkwayId);

            // then
            assertThat(response.walkways().size()).isEqualTo(0);
        }
    }


    @Nested
    @DisplayName("getUserWalkwayDetail 메소드는")
    class Describe_getUserWalkwayDetail{
        @Test
        @DisplayName("산책로가 존재하면 DTO로 변환한다.")
        void it_returns_response_DTO(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 6L;
            List<Walkway> walkways = IntStream.range(0, 5)
                    .mapToObj(index ->
                            createWalkwayWithId((long)(index+1), null)
                    ).toList();
            when(walkwayQueryService.getUserWalkWay(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            GetWalkwayDetailResponse response = userWalkwayUseCase.getUserWalkwayDetail(memberId, size, walkwayId);

            // then
            assertThat(response.walkways().size()).isEqualTo(walkways.size());
            for(int i=0; i<response.walkways().size(); i++){
                GetWalkwayDetailInfo walkwayInfo = response.walkways().get(i);
                Walkway walkway = walkways.get(i);
                assertThat(walkwayInfo.walkwayId()).isEqualTo(walkway.getId());
                assertThat(walkwayInfo.name()).isEqualTo(walkway.getName());
                assertThat(walkwayInfo.hashtags().size()).isEqualTo(walkway.getHashtagWalkways().size());
            }
        }

        @Test
        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 1L;
            List<Walkway> walkways = Collections.emptyList();
            when(walkwayQueryService.getUserWalkWay(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            GetWalkwayDetailResponse response = userWalkwayUseCase.getUserWalkwayDetail(memberId, size, walkwayId);

            // then
            assertThat(response.walkways().size()).isEqualTo(0);
        }

    }
}