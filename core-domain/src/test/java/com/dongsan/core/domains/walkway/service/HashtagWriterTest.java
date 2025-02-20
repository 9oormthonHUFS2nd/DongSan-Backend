//package com.dongsan.domains.walkway.service;
//
//import com.dongsan.domains.hashtag.entity.Hashtag;
//import com.dongsan.domains.hashtag.repository.HashtagRepository;
//import com.dongsan.core.domains.walkway.service.HashtagWriter;
//import fixture.HashtagFixture;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("HashtagCommandService Unit Test")
//class HashtagWriterTest {
//
//    @Mock
//    private HashtagRepository hashtagRepository;
//
//    @InjectMocks
//    private HashtagWriter hashtagWriter;
//
//    @Nested
//    @DisplayName("createHashtag 메서드는")
//    class createHashtag {
//
//        @Test
//        @DisplayName("Hashtag를 받으면 저장하고 Hashtag를 반환한다.")
//        void it_returns_hashtag() {
//            // Given
//            Hashtag hashtag = HashtagFixture.createHashtag("tag1");
//
//            when(hashtagRepository.save(hashtag)).thenReturn(hashtag);
//
//            // When
//            Hashtag result = hashtagWriter.createHashtag(hashtag);
//
//            // Then
//            Assertions.assertThat(result).isEqualTo(hashtag);
//        }
//    }
//}