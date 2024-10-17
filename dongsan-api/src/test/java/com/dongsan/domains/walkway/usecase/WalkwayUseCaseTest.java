package com.dongsan.domains.walkway.usecase;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.service.HashtagCommandService;
import com.dongsan.domains.hashtag.service.HashtagQueryService;
import com.dongsan.domains.hashtag.service.HashtagWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import fixture.HashtagFixture;
import fixture.HashtagWalkwayFixture;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalkwayUseCaseTest {

    @Mock
    private WalkwayCommandService walkwayCommandService;
    @Mock
    private MemberQueryService memberQueryService;
    @Mock
    private HashtagWalkwayCommandService hashtagWalkwayCommandService;
    @Mock
    private HashtagQueryService hashtagQueryService;
    @Mock
    private HashtagCommandService hashtagCommandService;
    @InjectMocks
    private WalkwayUseCase walkwayUseCase;

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


            when(memberQueryService.readMember(memberId)).thenReturn(Optional.of(member));
            when(walkwayCommandService.createWalkway(any()))
                    .thenReturn(walkway);
            when(hashtagQueryService.getHashtagsByName(any())).thenReturn(Collections.emptyList());
            when(hashtagWalkwayCommandService.createHashtagWalkways(any())).thenReturn(Collections.emptyList());

            // When
            CreateWalkwayResponse result =
                    walkwayUseCase.createWalkway(createWalkwayRequest, memberId);

            // Then
            Assertions.assertThat(result.walkwayId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("createHashtagWalkways 메서드는")
    class Describe_createHashtagWalkways {

        @Test
        @DisplayName("hashtag 이름 목록을 입력 받으면 hashtag와 hashtagWalkway를 생성 후 hashtagWalkway리스트를 반환한다.")
        void it_returns_hashtagWalkway_list() {
            // Given
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            List<String> hashtagNames = List.of("tag0", "tag1", "tag2");
            List<Hashtag> hashtags = new ArrayList<>();
            List<HashtagWalkway> hashtagWalkways =  new ArrayList<>();

            for(int i =1; i < 3; i++) {
                hashtags.add(HashtagFixture.createHashtagWithId((long)i, hashtagNames.get(i)));
            }
            for(int i =0; i < 3; i++) {
                hashtagWalkways.add(HashtagWalkwayFixture.createHashtagWalkwayWithId(
                        walkway,
                        hashtags.get(0),
                        (long)i
                ));
            }

            when(hashtagQueryService.getHashtagsByName(hashtagNames)).thenReturn(hashtags);
            when(hashtagCommandService.createHashtag(any())).thenReturn(null);
            when(hashtagWalkwayCommandService.createHashtagWalkways(any())).thenReturn(hashtagWalkways);

            // When
            List<HashtagWalkway> result = walkwayUseCase.createHashtagWalkways(walkway, hashtagNames);

            // Then
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.size()).isEqualTo(hashtagWalkways.size());
            Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(hashtagWalkways);
        }
    }
}