package com.dongsan.domains.walkway.usecase;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.exception.CustomException;
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
@DisplayName("LikedWalkwayUseCase Unit Test")
class LikedWalkwayUseCaseTest {
    @Mock
    LikedWalkwayCommandService likedWalkwayCommandService;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    WalkwayQueryService walkwayQueryService;
    @InjectMocks
    LikedWalkwayUseCase likedWalkwayUseCase;

    @Nested
    @DisplayName("createLikedWalkway 메서드는")
    class Describe_createLikedWalkway {
        @Test
        @DisplayName("회원과 산책로가 존재하면 좋아요를 생성한다.")
        void it_create_liked() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            when(memberQueryService.readMember(member.getId())).thenReturn(Optional.of(member));
            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(Optional.of(walkway));
            when(likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway)).thenReturn(true);

            // When & Then
            assertThatCode(() -> likedWalkwayUseCase.createLikedWalkway(member.getId(), walkway.getId()))
                    .doesNotThrowAnyException();

        }

        @Test
        @DisplayName("회원이 존재하지 않으면 예외를 반환한다.")
        void it_returns_MEMBER_NOT_FOUND() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            when(memberQueryService.readMember(member.getId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> likedWalkwayUseCase.createLikedWalkway(member.getId(), walkway.getId()))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        @DisplayName("회원이 존재하지 않으면 예외를 반환한다.")
        void it_returns_WALKWAY_NOT_FOUND() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            when(memberQueryService.readMember(member.getId())).thenReturn(Optional.of(member));
            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> likedWalkwayUseCase.createLikedWalkway(member.getId(), walkway.getId()))
                    .isInstanceOf(CustomException.class);
        }
    }
}