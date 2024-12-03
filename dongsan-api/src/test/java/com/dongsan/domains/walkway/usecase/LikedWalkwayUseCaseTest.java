package com.dongsan.domains.walkway.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
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

            when(memberQueryService.getMember(member.getId())).thenReturn(member);
            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
            when(likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway)).thenReturn(false);

            // When
            likedWalkwayUseCase.createLikedWalkway(member.getId(), walkway.getId());

            // Then
            verify(likedWalkwayCommandService).createLikedWalkway(any());
        }
    }

    @Nested
    @DisplayName("deleteLikedWalkway 메서드는")
    class Describe_deleteLikedWalkway {
        @Test
        @DisplayName("회원과 산책로가 존재하면 좋아요를 삭제한다.")
        void it_delete_liked() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            when(memberQueryService.getMember(member.getId())).thenReturn(member);
            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
            when(likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway)).thenReturn(true);

            // When
            likedWalkwayUseCase.deleteLikedWalkway(member.getId(), walkway.getId());

            // Then
            verify(likedWalkwayCommandService).deleteLikedWalkway(member, walkway);
        }
    }
}