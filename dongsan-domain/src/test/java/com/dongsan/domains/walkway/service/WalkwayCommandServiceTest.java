package com.dongsan.domains.walkway.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayCommandService Unit Test")
class WalkwayCommandServiceTest {

    @Mock
    private WalkwayRepository walkwayRepository;

    @InjectMocks
    private WalkwayCommandService walkwayCommandService;

    @Nested
    @DisplayName("createWalkway 메서드는")
    class Describe_createWalkway {

        @Test
        @DisplayName("Walkway를 받으면 저장하고 반환한다.")
        void it_returns_walkway() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            Walkway walkwayResult = WalkwayFixture.createWalkwayWithId(1L, member);

            when(walkwayRepository.save(walkway)).thenReturn(walkwayResult);

            // When
            Walkway result = walkwayCommandService.createWalkway(walkway);

            // Then
            Assertions.assertThat(result).isEqualTo(walkwayResult);
        }
    }
}