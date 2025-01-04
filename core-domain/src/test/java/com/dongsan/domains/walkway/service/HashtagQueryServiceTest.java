package com.dongsan.domains.walkway.service;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.repository.HashtagRepository;
import com.dongsan.domains.walkway.service.HashtagQueryService;
import fixture.HashtagFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashtagQueryService Unit Test")
class HashtagQueryServiceTest {

    @Mock
    private HashtagRepository hashtagRepository;

    @InjectMocks
    private HashtagQueryService hashtagQueryService;

    @Nested
    @DisplayName("getHashtagsByName 메서드는")
    class getHashtagsByName {

        @Test
        @DisplayName("hashtag 이름 목록을 받으면 같은 이름의 hashtag 리스트를 반환한다.")
        void it_returns_hashtag_list() {
            // Given
            List<String> names = new ArrayList<>();
            List<Hashtag> hashtags = new ArrayList<>();

            for(int i=0; i<10; i++) {
                names.add("name"+i);
                hashtags.add(HashtagFixture.createHashtag("name"+i));
            }

            when(hashtagRepository.findByNameIn(names)).thenReturn(hashtags);

            // When
            List<Hashtag> result = hashtagQueryService.getHashtagsByName(names);

            // Then
            Assertions.assertThat(result).containsExactlyElementsOf(hashtags);
        }
    }
}