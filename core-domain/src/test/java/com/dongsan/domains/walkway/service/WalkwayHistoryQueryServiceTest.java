package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.repository.WalkwayHistoryRepository;
import fixture.WalkwayHistoryFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayHistoryQueryService Unit Test")
class WalkwayHistoryQueryServiceTest {
    @InjectMocks
    WalkwayHistoryQueryService walkwayHistoryQueryService;
    @Mock
    WalkwayHistoryRepository walkwayHistoryRepository;

    @Nested
    @DisplayName("findByWalkwayAndMember 메서드는")
    class Describe_findByWalkwayAndMember {

        @Test
        @DisplayName("walkwayId와 memberId를 받으면 해당하는 WalkwayHistory를 반환한다.")
        void it_returns_walkway_history() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            WalkwayHistory walkwayHistory = WalkwayHistoryFixture.createWalkwayHistory(null, null);

            when(walkwayHistoryRepository.findTop1ByWalkwayIdAndMemberIdOrderByCreatedAtDesc(walkwayId, memberId))
                    .thenReturn(Optional.of(walkwayHistory));

            // when
            WalkwayHistory result = walkwayHistoryQueryService.findByWalkwayAndMember(walkwayId, memberId);

            // then
            assertThat(result).isEqualTo(walkwayHistory);
        }

        @Test
        @DisplayName("walkwayId와 memberId에 해당하는 WalkwayHistory가 없으면 예외를 발생시킨다.")
        void it_throws_exception_when_not_found() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;

            when(walkwayHistoryRepository.findTop1ByWalkwayIdAndMemberIdOrderByCreatedAtDesc(walkwayId, memberId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> walkwayHistoryQueryService.findByWalkwayAndMember(walkwayId, memberId))
                    .isInstanceOf(CustomException.class);
        }
    }
}