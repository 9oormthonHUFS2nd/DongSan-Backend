package com.dongsan.rdb.domains.walkway;

import com.dongsan.common.support.RepositoryTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("WalkwayHistoryQueryDSLRepository Unit Test")
class WalkwayHistoryQueryDSLRepositoryTest extends RepositoryTest {
//    @Autowired
//    TestEntityManager em;
//    @Autowired
//    WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getCanReviewWalkwayHistories 메서드는")
//    class Describe_getCanReviewWalkwayHistories {
//        Member member;
//        Walkway walkway;
//        @BeforeEach
//        void setUp() {
//            member = MemberFixture.createMember();
//            walkway = WalkwayFixture.createWalkway(member);
//            em.persist(member);
//            em.persist(walkway);
//            for (int i = 0; i < 5; i++) {
//                WalkwayHistory history = WalkwayHistoryFixture.createWalkwayHistory(member, walkway, 1.8, 10);
//                em.persist(history);
//            }
//        }
//
//        @Test
//        @DisplayName("해당 회원이 산책한 기록 중 리뷰 가능 거리(2/3 이상)이며, 아직 리뷰하지 않은 기록만 반환한다.")
//        void it_returns_can_review_walkway_histories() {
//            // given
//            Long memberId = member.getId();
//            Long walkwayId = walkway.getId();
//
//            // when
//            List<WalkwayHistory> result = walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId);
//
//            // then
//            assertThat(result).hasSize(5);
//
//            for (WalkwayHistory history : result) {
//                assertThat(history.getDistance()).isGreaterThanOrEqualTo(walkway.getDistance() * (2.0 / 3.0));
//                assertThat(history.getIsReviewed()).isFalse();
//            }
//        }
//    }
}