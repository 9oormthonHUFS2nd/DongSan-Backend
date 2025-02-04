package com.dongsan.domains.walkway.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.mapper.WalkwayHistoryMapper;
import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import fixture.WalkwayHistoryFixture;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayHistoryUseCase Unit Test")
class WalkwayHistoryUseCaseTest {

    @InjectMocks
    WalkwayHistoryUseCase walkwayHistoryUseCase;
    @Mock
    WalkwayHistoryCommandService walkwayHistoryCommandService;
    @Mock
    WalkwayQueryService walkwayQueryService;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    WalkwayHistoryMapper walkwayHistoryMapper;
    @Mock
    WalkwayHistoryQueryService walkwayHistoryQueryService;

    @Nested
    @DisplayName("createWalkwayHistory 메서드는")
    class Describe_createWalkwayHistory {

        @Test
        @DisplayName("memberId와 walkwayId를 받아 WalkwayHistory를 생성하고 저장된 ID를 반환한다.")
        void it_creates_and_returns_walkway_history_id() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            Long walkwayHistoryId = 100L;

            Walkway walkway = WalkwayFixture.createWalkway(null);
            Member member = MemberFixture.createMember();
            CreateWalkwayHistoryRequest request = new CreateWalkwayHistoryRequest(600, 10.0);
            WalkwayHistory walkwayHistory = WalkwayHistoryFixture.createWalkwayHistoryWithId(walkwayHistoryId,null, null);

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(walkwayHistoryCommandService.createWalkwayHistory(any())).thenReturn(walkwayHistory);

            // when
            Long result = walkwayHistoryUseCase.createWalkwayHistory(memberId, walkwayId, request);

            // then
            assertThat(result).isEqualTo(walkwayHistoryId);
        }
    }

    @Nested
    @DisplayName("getWalkwayHistories 메서드는")
    class Describe_getWalkwayHistories {

        @Test
        @DisplayName("공개된 산책로인 경우, 리뷰 가능한 WalkwayHistory 목록을 반환한다.")
        void it_returns_walkway_histories() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, null);

            List<WalkwayHistory> histories = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                histories.add(WalkwayHistoryFixture.createWalkwayHistory(null, walkway));
            }

            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(walkwayHistoryQueryService.getCanReviewWalkwayHistories(walkwayId, memberId))
                    .thenReturn(histories);

            // when
            List<WalkwayHistory> result = walkwayHistoryUseCase.getWalkwayHistories(memberId, walkwayId);

            // then
            assertThat(result).hasSize(3);
        }
    }
}