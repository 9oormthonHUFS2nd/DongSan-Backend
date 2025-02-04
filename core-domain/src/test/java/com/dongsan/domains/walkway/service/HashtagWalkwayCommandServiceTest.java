package com.dongsan.domains.walkway.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.repository.HashtagWalkwayRepository;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.service.HashtagWalkwayCommandService;
import fixture.HashtagFixture;
import fixture.HashtagWalkwayFixture;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashtagWalkwayCommandService Unit Test")
class HashtagWalkwayCommandServiceTest {

    @Mock
    private HashtagWalkwayRepository hashtagWalkwayRepository;

    @InjectMocks
    private HashtagWalkwayCommandService hashtagWalkwayCommandService;

    @Nested
    @DisplayName("createHashtagWalkways 메서드는")
    class createHashtagWalkways {

        @Test
        @DisplayName("HashtagWalkway 리스트를 받으면 모두 저장하고 HashtagWalkway 리스트를 반환한다.")
        void it_returns_hashtagwalkway_list() {
            // Given
            List<HashtagWalkway> hashtags = new ArrayList<>();
            Member member = MemberFixture.createMember();
            Walkway walkway = WalkwayFixture.createWalkway(member);
            for(int i=0; i<10; i++) {
                Hashtag hashtag = HashtagFixture.createHashtag("tag"+i);
                HashtagWalkway hashtagWalkway = HashtagWalkwayFixture.createHashtagWalkway(walkway, hashtag);
                hashtags.add(hashtagWalkway);
            }

            when(hashtagWalkwayRepository.saveAll(hashtags)).thenReturn(hashtags);

            // When
            List<HashtagWalkway> result = hashtagWalkwayCommandService.createHashtagWalkways(hashtags);

            // Then
            Assertions.assertThat(result).containsExactlyElementsOf(hashtags);
        }
    }

    @Nested
    @DisplayName("deleteAllHashtagWalkways 메서드는")
    class Describe_deleteAllHashtagWalkways {
        @Test
        @DisplayName("산책로의 모든 해쉬태그를 삭제한다.")
        void it_delete_all_hashtags() {
            // Given
            Walkway walkway = WalkwayFixture.createWalkway(null);
            // When
            hashtagWalkwayCommandService.deleteAllHashtagWalkways(walkway);
            // Then
            verify(hashtagWalkwayRepository).deleteAllByWalkway(walkway);
        }
    }
}