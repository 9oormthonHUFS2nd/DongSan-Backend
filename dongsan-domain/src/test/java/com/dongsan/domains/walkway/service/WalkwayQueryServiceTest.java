package com.dongsan.domains.walkway.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import fixture.MemberFixture;
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
@DisplayName("WalkwayQueryService Unit Test")
class WalkwayQueryServiceTest {

    @Mock
    WalkwayRepository walkwayRepository;
    @Mock
    WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @InjectMocks
    WalkwayQueryService walkwayQueryService;

    @Nested
    @DisplayName("getWalkwayWithLiked 메서드는")
    class Describe_getWalkwayWithLiked {

        @Test
        @DisplayName("walkwayId를 받으면 해당하는 Walkway와 Liked 리스트를 반환한다.")
        void it_returns_walkway_liked() {
            // given
            Member member = MemberFixture.createMemberWithId(1L);
            Walkway walkway = createWalkwayWithId(1L, member);

            // Mocking Tuple 반환값 설정
            when(walkwayQueryDSLRepository.getWalkway(member.getId(), walkway.getId())).thenReturn(walkway);

            // when
            Walkway result = walkwayQueryService.getWalkwayWithRatingAndLike(member.getId(), walkway.getId());

            // then
            assertThat(result).isEqualTo(walkway);
        }
    }

    @Nested
    @DisplayName("getUserWalkWay 메서드는")
    class Describe_getUserWalkWay{
        @Test
        @DisplayName("walkwayId보다 Id가 작은 walkway가 있으면 walkway를 리스트로 반환한다.")
        void it_returns_walkway_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 3L;
            List<Walkway> walkways = IntStream.range(0, 2)
                    .mapToObj(index ->
                            createWalkwayWithId((long)(index+1), null)
                    ).toList();
            when(walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayQueryService.getUserWalkWay(memberId, size, walkwayId);

            // then
            assertThat(result).isEqualTo(walkways);
        }

        @Test
        @DisplayName("walkwayId보다 Id가 작은 walkway가 없으면 빈 리스트를 반환한다.")
        void it_returns_emtpy_list(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 3L;
            List<Walkway> walkways = Collections.emptyList();
            when(walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId)).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayQueryService.getUserWalkWay(memberId, size, walkwayId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByWalkwayId 메서드는")
    class Describe_existsByWalkwayId{
        @Test
        @DisplayName("walkway가 존재하면 true를 반환한다.")
        void it_returns_true(){
            // given
            Long walkwayId = 1L;
            when(walkwayRepository.existsById(walkwayId)).thenReturn(true);

            // when
            boolean result = walkwayQueryService.existsByWalkwayId(walkwayId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("walkway가 존재하지 않으면 false를 반환한다.")
        void it_returns_false(){
            // given
            Long walkwayId = 1L;
            when(walkwayRepository.existsById(walkwayId)).thenReturn(false);

            // when
            boolean result = walkwayQueryService.existsByWalkwayId(walkwayId);

            // then
            assertThat(result).isFalse();
        }
    }
}