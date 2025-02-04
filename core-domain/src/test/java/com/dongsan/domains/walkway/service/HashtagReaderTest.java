package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.walkway.service.HashtagReader;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.repository.HashtagRepository;
import fixture.HashtagFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("HashtagQueryService Unit Test")
class HashtagReaderTest {

    @Mock
    private HashtagRepository hashtagRepository;

    @InjectMocks
    private HashtagReader hashtagReader;

    @Nested
    @DisplayName("getHashtagsByName 메서드는")
    class Describe_getHashtagsByName {

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
            List<Hashtag> result = hashtagReader.getHashtagsByName(names);

            // Then
            assertThat(result).containsExactlyElementsOf(hashtags);
        }
    }

    @Nested
    @DisplayName("findByNameOptional 메서드는")
    class Describe_findByNameOptional {
        @Test
        @DisplayName("해쉬태그가 입력 되면 해당 해쉬태그 엔티티를 반환한다.")
        void it_returns_entity() {
            // Given
            String hashtagName = "testTag";
            Hashtag hashtag = HashtagFixture.createHashtag(hashtagName);
            when(hashtagRepository.findByName(hashtagName)).thenReturn(Optional.of(hashtag));

            // When
            Optional<Hashtag> result = hashtagReader.findByNameOptional(hashtagName);

            // Then
            assertThat(result).isNotNull();
        }
    }
}