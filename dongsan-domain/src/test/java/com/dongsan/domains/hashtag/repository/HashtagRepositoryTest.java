package com.dongsan.domains.hashtag.repository;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.hashtag.entity.Hashtag;
import fixture.HashtagFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

@DisplayName("HashtagRepository Unit Test")
class HashtagRepositoryTest extends RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HashtagRepository hashtagRepository;

    @BeforeEach
    void setUpContext() {
        for(int i=0; i<10; i++) {
            Hashtag hashtag = HashtagFixture.createHashtag("tag"+i);
            entityManager.persist(hashtag);
        }
    }

    @Nested
    @DisplayName("findByNameIn 메서드는")
    class Describe_findByNameIn {

        @Test
        @DisplayName("이름이 같은 태그가 있으면 모아서 리스트로 출력한다.")
        void it_returns_hashtag_list() {
            // Given
            List<String> names = new ArrayList<>();
            for(int i=0; i<10; i++) {
                names.add("tag"+i);
            }

            // When
            List<Hashtag> hashtags = hashtagRepository.findByNameIn(names);

            // Then
            for(int i=0; i<10; i++) {
                Assertions.assertThat(hashtags.get(i).getName()).isEqualTo(names.get(i));
            }
        }

        @Test
        @DisplayName("이름이 같은 태그가 없으면 빈 리스트를 출력한다.")
        void it_returns_empty_list() {
            // Given
            List<String> names = new ArrayList<>();
            for(int i=0; i<10; i++) {
                names.add("not exist tag"+i);
            }

            // When
            List<Hashtag> hashtags = hashtagRepository.findByNameIn(names);

            // Then
            Assertions.assertThat(hashtags).isEmpty();
        }
    }
}