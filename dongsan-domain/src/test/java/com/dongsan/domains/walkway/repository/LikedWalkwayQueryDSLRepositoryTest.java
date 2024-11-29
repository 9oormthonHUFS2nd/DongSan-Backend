package com.dongsan.domains.walkway.repository;

import static fixture.LikedWalkwayFixture.createLikedWalkway;
import static fixture.MemberFixture.createMember;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("LikedWalkwayQueryDSLRepository Unit Test")
class LikedWalkwayQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway{
        @BeforeEach
        void setUp(){
            Member member = createMember();
            em.persist(member);
            for(int i=0; i<5; i++){
                Walkway walkway = createWalkway(member);
                LikedWalkway likedWalkway = createLikedWalkway(member, walkway);
                em.persist(walkway);
                em.persist(likedWalkway);
            }
        }

        @Test
        @DisplayName("likedWalkwayId가 null이면 가장 최근의 likedWalkway들을 내림차순으로 가져온다.")
        void it_returns_most_recent_likeWalkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long likedWalkwayId = null;

            // when
            List<LikedWalkway> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkwayId);

            // then
            assertThat(result).hasSize(5);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("likedWalkwayId가 null이 아니면 likedWalkwayId보다 일찍 등록한 likedWalkway를 내림차순으로 가져온다.")
        void it_returns_next_likeWalkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long likedWalkwayId = 3L;

            // when
            List<LikedWalkway> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkwayId);

            // then
            assertThat(result).hasSize(2);
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("member가 좋아요한 LikedWalkway가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list_when_member_not_liked(){
            // given
            Long memberId = 2L;
            Integer size = 5;
            Long likedWalkwayId = 6L;

            // when
            List<LikedWalkway> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkwayId);

            // then
            assertThat(result).hasSize(0);
        }

        @Test
        @DisplayName("주어진 likedWalkwayId보다 작은 Id의 likedWalkway가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list_when_lt_likedWalkway_not_exist(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            Long likedWalkwayId = 1L;

            // when
            List<LikedWalkway> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkwayId);

            // then
            assertThat(result).hasSize(0);
        }
    }
}