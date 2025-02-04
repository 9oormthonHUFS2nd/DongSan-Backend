package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.LikedWalkwayRepository;
import fixture.LikedWalkwayFixture;
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
@DisplayName("LikedWalkwayCommandService Unit Test")
class LikedWalkwayCommandServiceTest {

    @InjectMocks
    LikedWalkwayCommandService likedWalkwayCommandService;
    @Mock
    LikedWalkwayRepository likedWalkwayRepository;

    @Nested
    @DisplayName("createLikedWalkway 메서드는")
    class Describe_createLikedWalkway {
        @Test
        @DisplayName("LikedWalkway를 저장하고 반환한다.")
        void it_returns_likedWalkway() {
            // Given
            LikedWalkway likedWalkway = LikedWalkwayFixture.createLikedWalkway(null, null);
            when(likedWalkwayRepository.save(likedWalkway)).thenReturn(likedWalkway);

            // When
            LikedWalkway result = likedWalkwayCommandService.createLikedWalkway(likedWalkway);

            // Then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("existsLikedWalkwayByMemberAndWalkway 메서드는")
    class Describe_existsLikedWalkwayByMemberAndWalkway {
        @Test
        @DisplayName("Member와 Walkway로 LikedWalkway의 유무를 반환한다.")
        void it_returns_exists() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            when(likedWalkwayRepository.existsByMemberAndWalkway(member, walkway)).thenReturn(true);

            // When
            Boolean result = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);

            // Then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("deleteLikedWalkway 메서드는")
    class Describe_deleteLikedWalkway {
        @Test
        @DisplayName("Member와 Walkway로 LikedWalkway를 삭제한다.")
        void it_delete_likedWalkway() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);

            // When & Then
            assertThatCode(() -> likedWalkwayCommandService.deleteLikedWalkway(member, walkway))
                    .doesNotThrowAnyException();
        }
    }
}