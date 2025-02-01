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
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import fixture.WalkwayHistoryFixture;
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
}