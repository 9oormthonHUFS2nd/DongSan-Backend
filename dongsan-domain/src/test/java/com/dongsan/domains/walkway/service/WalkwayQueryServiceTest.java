package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalkwayQueryServiceTest {

    @Mock
    private WalkwayRepository walkwayRepository;

    @InjectMocks
    private WalkwayQueryService walkwayQueryService;

    @Nested
    @DisplayName("getWalkwayWithLiked 메서드는")
    class Describe_getWalkwayWithLiked {

        @Test
        @DisplayName("walkwayId를 받으면 해당하는 Walkway와 Liked 리스트를 반환한다.")
        void it_returns_walkway_liked() {
            // given
            Member member = MemberFixture.createMemberWithId(1L);
            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, member);

            // Mocking Tuple 반환값 설정
            when(walkwayRepository.findById(walkway.getId())).thenReturn(Optional.of(walkway));

            // when
            Optional<Walkway> result = walkwayQueryService.getWalkway(walkway.getId());

            // then
            assertThat(result.get()).isEqualTo(walkway);
        }
    }
}