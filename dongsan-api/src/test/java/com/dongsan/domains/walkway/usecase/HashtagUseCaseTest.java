package com.dongsan.domains.walkway.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.service.HashtagCommandService;
import com.dongsan.domains.hashtag.service.HashtagQueryService;
import com.dongsan.domains.hashtag.service.HashtagWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import fixture.HashtagFixture;
import fixture.HashtagWalkwayFixture;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashtagUseCaseTest Unit Test")
class HashtagUseCaseTest {
    @Mock
    HashtagWalkwayCommandService hashtagWalkwayCommandService;
    @Mock
    HashtagQueryService hashtagQueryService;
    @Mock
    HashtagCommandService hashtagCommandService;
    @InjectMocks
    HashtagUseCase hashtagUseCase;


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
            List<HashtagWalkway> hashtagWalkways = new ArrayList<>();

            for (int i = 1; i < 3; i++) {
                hashtags.add(HashtagFixture.createHashtagWithId((long) i, hashtagNames.get(i)));
            }
            for (int i = 0; i < 3; i++) {
                hashtagWalkways.add(HashtagWalkwayFixture.createHashtagWalkwayWithId(
                        walkway,
                        hashtags.get(0),
                        (long) i
                ));
            }

            when(hashtagQueryService.getHashtagsByName(hashtagNames)).thenReturn(hashtags);
            when(hashtagCommandService.createHashtag(any())).thenReturn(null);
            when(hashtagWalkwayCommandService.createHashtagWalkways(any())).thenReturn(hashtagWalkways);

            // When
            List<HashtagWalkway> result = hashtagUseCase.createHashtagWalkways(walkway, hashtagNames);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(hashtagWalkways.size());
            assertThat(result).containsExactlyInAnyOrderElementsOf(hashtagWalkways);
        }
    }

}