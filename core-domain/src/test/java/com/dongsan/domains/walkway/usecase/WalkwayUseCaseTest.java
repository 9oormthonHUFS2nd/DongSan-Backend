package com.dongsan.domains.walkway.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.walkway.service.HashtagQueryService;
import com.dongsan.domains.walkway.service.HashtagWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayUseCase Unit Test")
class WalkwayUseCaseTest {

    @Mock
    WalkwayCommandService walkwayCommandService;
    @Mock
    WalkwayQueryService walkwayQueryService;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    HashtagWalkwayCommandService hashtagWalkwayCommandService;
    @Mock
    HashtagQueryService hashtagQueryService;
    @Mock
    HashtagUseCase hashtagUseCase;
    @InjectMocks
    WalkwayUseCase walkwayUseCase;

    @Nested
    @DisplayName("createWalkway 메서드는")
    class Describe_createWalkway {

        @Test
        @DisplayName("요청한 유저가 존재하면 산책로를 등록하고 산책로 id를 DTO로 반환한다.")
        void it_returns_responseDTO() {
            // Given
            Long memberId = 1L;

            Member member = MemberFixture.createMemberWithId(memberId);

            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
                    "testName",
                    "testMemo",
                    4.2,
                    20,
                    List.of("하나", "둘"),
                    "공개",
                    List.of(List.of(127.001, 37.001), List.of(127.002, 37.002))
            );

            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, member);

            when(memberQueryService.getMember(member.getId())).thenReturn(member);
            when(walkwayCommandService.createWalkway(any()))
                    .thenReturn(walkway);

            // When
            CreateWalkwayResponse result =
                    walkwayUseCase.createWalkway(createWalkwayRequest, memberId);

            // Then
            assertThat(result.walkwayId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("getWalkwayWithLiked 메서드는")
    class Describe_getWalkwayWithLiked {
        @Test
        @DisplayName("walkwayId에 해당하는 산책로가 없으면 예외처리 한다.")
        void it_returns_exception_not_found_walkway() {
            // given
            when(walkwayQueryService.getWalkwayWithHashtagAndLike(1L, 1L)).thenReturn(null);

            // when & then
            assertThrows(CustomException.class, () -> {
                walkwayUseCase.getWalkwayWithLiked(1L, 1L);
            });
        }

        @Test
        @DisplayName("walkwayId에 해당하는 산책로가 있으면 DTO를 반환한다.")
        void it_returns_DTO() {
            // given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, member);

            when(walkwayQueryService.getWalkwayWithHashtagAndLike(any(), any())).thenReturn(walkway);

            // when
            Walkway result = walkwayUseCase.getWalkwayWithLiked(walkway.getId(), member.getId());

            // then
            Assertions.assertThat(result).isEqualTo(walkway);
        }
    }

    @Nested
    @DisplayName("getWalkwaysSearch 메서드는")
    class Describe_getWalkwaysSearch {

        List<Walkway> walkways = new ArrayList<>();

        @BeforeEach
        void setUp() {
            Member member = MemberFixture.createMember();
            for (int i = 0; i < 10; i++) {
                walkways.add(WalkwayFixture.createWalkway(member));
            }
        }

        @Test
        @DisplayName("type이 liked면 좋아요 순으로 산책로를 반환한다.")
        void it_returns_walkways_popular() {
            // given
            Long userId = 1L;
            String type = "liked";
            Double latitude = 2.0;
            Double longitude = 2.0;
            Double distance = 10.0;
            String hashtags = "test0,test1";
            Long lastId = null;
            Walkway lastWalkway = null;
            int size = 10;

            List<String> hashtagsList = new ArrayList<>();
            Arrays.stream(hashtags.split(",")).forEach(hashtag -> hashtagsList.add(hashtag.trim()));

            int distanceInt = (int) (distance * 1000);

            SearchWalkwayPopular searchWalkwayPopular
                    = new SearchWalkwayPopular(userId, longitude, latitude, distanceInt, hashtagsList, lastWalkway, size);

            when(walkwayQueryService.getWalkwaysPopular(searchWalkwayPopular))
                    .thenReturn(walkways);

            // when
            List<Walkway> result
                    = walkwayUseCase.getWalkwaysSearch(userId, type, latitude, longitude, distance, hashtags, lastId, size);

            // then
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result).hasSize(size);
        }

        @Test
        @DisplayName("type이 rating이면 별점 순으로 산책로를 반환한다.")
        void it_returns_walkways_rating() {
            // given
            Long userId = 1L;
            String type = "rating";
            Double latitude = 2.0;
            Double longitude = 2.0;
            Double distance = 10.0;
            String hashtags = "test0,test1";
            Long lastId = null;
            Walkway lastWalkway = null;
            int size = 10;

            List<String> hashtagsList = new ArrayList<>();
            Arrays.stream(hashtags.split(",")).forEach(hashtag -> hashtagsList.add(hashtag.trim()));

            int distanceInt = (int) (distance * 1000);

            SearchWalkwayRating searchWalkwayRating = new SearchWalkwayRating(userId, longitude, latitude, distanceInt, hashtagsList, lastWalkway, size);
            when(walkwayQueryService.getWalkwaysRating(searchWalkwayRating))
                    .thenReturn(walkways);

            // when
            List<Walkway> result
                    = walkwayUseCase.getWalkwaysSearch(userId, type, latitude, longitude, distance, hashtags, lastId,
                    size);

            // then
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result).hasSize(10);
        }

        @Test
        @DisplayName("type이 잘 못 입력 되면 예외처리")
        void it_returns_exception() {
            // given
            Long userId = 1L;
            String type = "으헤헿헿헤헤헤ㅔ헤헤헤헤헿";
            Double latitude = 2.0;
            Double longitude = 2.0;
            Double distance = 10.0;
            String hashtags = "test0,test1";
            Long lastId = null;
            int size = 10;

            assertThatThrownBy(
                    () -> walkwayUseCase.getWalkwaysSearch(userId, type, latitude, longitude, distance, hashtags,
                            lastId, size))
                    .isInstanceOf(CustomException.class);
        }

    }

    @Nested
    @DisplayName("updateWalkway 메서드는")
    class Describe_updateWalkway {
        @Test
        @DisplayName("산책로를 등록한 회원가 다르면 예외처리한다.")
        void it_returns_exceptions() {
            // Given
            Long memberId = 1L;
            Long otherMemberId = 2L;
            Long walkwayId = 1L;
            Member otherMember = MemberFixture.createMemberWithId(otherMemberId);
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, otherMember);

            when(walkwayQueryService.getWalkwayWithHashtag(walkway.getId())).thenReturn(walkway);

            // When & Then
            assertThatThrownBy(() -> walkwayUseCase.updateWalkway(null, memberId, walkwayId))
                    .isInstanceOf(CustomException.class);

        }

        @Test
        @DisplayName("산책로를 수정한다.")
        void it_update_walkway() {
            // Given
            Member member = MemberFixture.createMemberWithId(1L);
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, member);
            UpdateWalkwayRequest updateWalkwayRequest = new UpdateWalkwayRequest(
                    "test name",
                    "test memo",
                    List.of(),
                    "비공개");

            when(walkwayQueryService.getWalkwayWithHashtag(walkway.getId())).thenReturn(walkway);
            when(walkwayCommandService.createWalkway(walkway)).thenReturn(null);

            // When
            walkwayUseCase.updateWalkway(updateWalkwayRequest, member.getId(), walkwayId);

            // Then
            Assertions.assertThat(walkway.getHashtagWalkways()).isEmpty();
            Assertions.assertThat(walkway.getName()).isEqualTo("test name");
            Assertions.assertThat(walkway.getMemo()).isEqualTo("test memo");
            Assertions.assertThat(walkway.getExposeLevel()).isEqualTo(ExposeLevel.PRIVATE);
        }
    }
}