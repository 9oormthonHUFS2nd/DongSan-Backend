package com.dongsan.domains.hashtag.repository;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static fixture.HashtagFixture.createHashtag;
import static fixture.HashtagWalkwayFixture.createHashtagWalkway;
import static fixture.MemberFixture.createMember;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

class HashtagDSLRepositoryTest extends RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HashtagDSLRepository hashtagDSLRepository;

    @Nested
    @DisplayName("getHashtagsByWalkwayId 메서드는")
    class Describe_getHashtagsByWalkwayId {

        @Test
        @DisplayName("walkwayId에 해당 하는 해쉬태그를 불러온다.")
        void it_returns_hashtags() {
            // given
            Member member = createMember();
            Walkway walkway = createWalkway(member);
            entityManager.persist(member);
            entityManager.persist(walkway);
            for(Long i = 0L; i < 3L; i++) {
                Hashtag hashtag = createHashtag("test"+i);
                entityManager.persist(hashtag);
                entityManager.persist(createHashtagWalkway(walkway, hashtag));
            }

            // when
            List<Hashtag> result = hashtagDSLRepository.getHashtagsByWalkwayId(walkway.getId());

            // then
            assertThat(result.size()).isEqualTo(3);
            for(int i = 0; i < 3; i++) {
                assertThat(result.get(i).getName()).isEqualTo("test"+i);
            }
        }
    }

}