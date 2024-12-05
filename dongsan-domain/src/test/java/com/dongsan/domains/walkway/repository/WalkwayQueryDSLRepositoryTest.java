package com.dongsan.domains.walkway.repository;

import static fixture.MemberFixture.createMember;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import fixture.HashtagFixture;
import fixture.HashtagWalkwayFixture;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("WalkwayQueryDSLRepositoryTest Unit Test")
class WalkwayQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @Nested
    @DisplayName("getUserWalkway 메소드는")
    class Describe_getUserWalkway{

        @BeforeEach
        void setUp(){
            Member member = createMember();
            em.persist(member);
            for(int i=0; i<5; i++){
                Walkway walkway = createWalkway(member);
                em.persist(walkway);
            }
        }

        @Test
        @DisplayName("walkwayId가 null이면 가장 최근의 walkway들을 내림차순으로 가져온다.")
        void it_returns_most_recent_walkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = null;

            // when
            List<Walkway> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId);

            // then
            assertThat(result).hasSize(5);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("walkwayId가 null이 아니면 walkwayId보다 일찍 등록한 walkway를 내림차순으로 가져온다.")
        void it_returns_next_walkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long walkwayId = 4L;

            // when
            List<Walkway> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId);

            // then
            assertThat(result).hasSize(3);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 2L;
            Integer size = 5;
            Long walkwayId = 6L;

            // when
            List<Walkway> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getUserWalkwayWithHashtagAndLike 메서드는")
    class Describe_getUserWalkwayWithHashtagAndLike {

        Member member;
        Walkway walkway;
        @BeforeEach
        void setUp(){
            member = createMember();
            em.persist(member);
            walkway = createWalkway(member);
            em.persist(walkway);
        }

        @Test
        @DisplayName("해쉬태그와 좋아요와 함께 산책로를 반환한다.")
        void it_returns_walkway_with_hashtags_liked() {
            // Given
            Long memberId = member.getId();
            Long walkwayId = walkway.getId();

            // When
            Walkway result = walkwayQueryDSLRepository.getUserWalkwayWithHashtagAndLike(memberId, walkwayId);

            // Then
            assertThat(result).isEqualTo(walkway);
            assertThat(result.getHashtagWalkways()).isNotNull();
        }
    }

    @Nested
    @DisplayName("getWalkwayWithHashtag 메서드는")
    class Describe_getWalkwayWithHashtag {
        @Test
        @DisplayName("Walkway를 hashtag, member와 함께 반환한다.")
        void it_returns_walkway() {
            // Given
            Member member = MemberFixture.createMember();
            em.persist(member);
            Walkway walkway = WalkwayFixture.createWalkway(member);
            em.persist(walkway);
            Hashtag hashtag = HashtagFixture.createHashtag("test");
            em.persist(hashtag);
            HashtagWalkway hashtagWalkway = HashtagWalkwayFixture.createHashtagWalkway(walkway, hashtag);
            walkway.addHashtagWalkway(hashtagWalkway);
            em.persist(hashtagWalkway);

            // When
            Walkway result = walkwayQueryDSLRepository.getWalkwayWithHashtag(walkway.getId());

            // Then
            assertThat(result.getId()).isEqualTo(walkway.getId());
            assertThat(result.getMember()).isEqualTo(member);
            assertThat(result.getHashtagWalkways()).hasSize(4);
        }
    }
}