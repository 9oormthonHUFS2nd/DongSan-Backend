package com.dongsan.domains.walkway.repository;

import static fixture.LikedWalkwayFixture.createLikedWalkway;
import static fixture.MemberFixture.createMember;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.annotation.RepositoryTest;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.QLikedWalkway;
import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@RepositoryTest
class WalkwayQueryDSLRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @Nested
    @DisplayName("getWalkwayWithLiked 메서드는")
    class Describe_getWalkwayWithLiked {
        Member member;
        Walkway walkwayWithLikedWalkway;
        Walkway walkwayWithOutLikedWalkway;
        LikedWalkway likedWalkway;

        @BeforeEach
        void setUp() {
            member = createMember();
            walkwayWithLikedWalkway = createWalkway(member);
            walkwayWithOutLikedWalkway = createWalkway(member);
            likedWalkway = createLikedWalkway(member, walkwayWithLikedWalkway);
            entityManager.persist(member);
            entityManager.persist(walkwayWithLikedWalkway);
            entityManager.persist(walkwayWithOutLikedWalkway);
            entityManager.persist(likedWalkway);
        }

        @Test
        @DisplayName("walkwayId에 해당하고 liked가 아닌 산책로를 불러온다.")
        void it_returns_walkway_without_liked() {
            // given
            Long memberId = member.getId();
            Long walkwayId = walkwayWithOutLikedWalkway.getId();

            // when
            Tuple result = walkwayQueryDSLRepository.getWalkwayWithLiked(walkwayId, memberId);

            // then
            assertThat(result.get(QWalkway.walkway)).isEqualTo(walkwayWithOutLikedWalkway);
            assertThat(result.get(QLikedWalkway.likedWalkway)).isNull();
        }

        @Test
        @DisplayName("walkwayId에 해당하고 liked인 산책로와 liked를 불러온다")
        void it_returns_walkway_with_liked() {
            // given
            Long memberId = member.getId();
            Long walkwayId = walkwayWithLikedWalkway.getId();

            // when
            Tuple result = walkwayQueryDSLRepository.getWalkwayWithLiked(walkwayId, memberId);

            // then
            assertThat(result.get(QWalkway.walkway)).isEqualTo(walkwayWithLikedWalkway);
            assertThat(result.get(QLikedWalkway.likedWalkway)).isNotNull();
            assertThat(result.get(QLikedWalkway.likedWalkway)).isEqualTo(likedWalkway);
        }
    }

}