//package com.dongsan.domains.hashtag.repository;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.walkway.repository.HashtagEntity;
//import com.dongsan.rdb.domains.walkway.repository.HashtagJpaRepository;
//import fixture.HashtagFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("HashtagRepository Unit Test")
//class HashtagEntityRepositoryTest extends RepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private HashtagJpaRepository hashtagJpaRepository;
//
//    @Nested
//    @DisplayName("findByNameIn 메서드는")
//    class Describe_findByNameIn {
//
//        @BeforeEach
//        void setUp() {
//            for(int i=0; i<10; i++) {
//                HashtagEntity hashtagEntity = HashtagFixture.createHashtag("tag"+i);
//                entityManager.persist(hashtagEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("이름이 같은 태그가 있으면 모아서 리스트로 출력한다.")
//        void it_returns_hashtag_list() {
//            // Given
//            List<String> names = new ArrayList<>();
//            for(int i=0; i<10; i++) {
//                names.add("tag"+i);
//            }
//
//            // When
//            List<HashtagEntity> hashtagEntities = hashtagJpaRepository.findByNameIn(names);
//
//            // Then
//            for(int i=0; i<10; i++) {
//                Assertions.assertThat(hashtagEntities.get(i).getName()).isEqualTo(names.get(i));
//            }
//        }
//
//        @Test
//        @DisplayName("이름이 같은 태그가 없으면 빈 리스트를 출력한다.")
//        void it_returns_empty_list() {
//            // Given
//            List<String> names = new ArrayList<>();
//            for(int i=0; i<10; i++) {
//                names.add("not exist tag"+i);
//            }
//
//            // When
//            List<HashtagEntity> hashtagEntities = hashtagJpaRepository.findByNameIn(names);
//
//            // Then
//            Assertions.assertThat(hashtagEntities).isEmpty();
//        }
//    }
//}