//package com.dongsan.domains.walkway.repository;
//
//import static fixture.MemberFixture.createMember;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
//import com.dongsan.rdb.domains.walkway.repository.WalkwayQueryDSLRepository;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("WalkwayQueryDSLRepositoryTest Unit Test")
//class WalkwayEntityQueryDSLRepositoryTest extends RepositoryTest {
//    @Autowired
//    TestEntityManager em;
//
//    @Autowired
//    WalkwayQueryDSLRepository walkwayQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getUserWalkway 메소드는")
//    class Describe_getUserWalkwayEntity {
//
//        @BeforeEach
//        void setUp(){
//            MemberEntity memberEntity = createMember();
//            em.persist(memberEntity);
//            for(int i=0; i<5; i++){
//                WalkwayEntity walkwayEntity = createWalkway(memberEntity);
//                em.persist(walkwayEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreateAt가 null이면 가장 최근의 walkway들을 내림차순으로 가져온다.")
//        void it_returns_most_recent_walkways(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            LocalDateTime lastCreateAt = null;
//
//            // when
//            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);
//
//            // then
//            for (WalkwayEntity walkwayEntity : result) {
//                assertThat(walkwayEntity.getMemberEntity()
//                        .getId()).isEqualTo(memberId);
//            }
//            for(int i=1; i<result.size(); i++){
//                LocalDateTime after = result.get(i-1).getCreatedAt();
//                LocalDateTime prev = result.get(i).getCreatedAt();
//                assertThat(prev).isBeforeOrEqualTo(after);
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreateAt가 null이 아니면 walkwayId보다 일찍 등록한 walkway를 내림차순으로 가져온다.")
//        void it_returns_next_walkways(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            LocalDateTime lastCreateAt = LocalDateTime.now().minusSeconds(1L);
//
//            // when
//            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);
//
//            // then
//            for (WalkwayEntity walkwayEntity : result) {
//                assertThat(walkwayEntity.getMemberEntity()
//                        .getId()).isEqualTo(memberId);
//                assertThat(walkwayEntity.getCreatedAt()).isBefore(lastCreateAt);
//            }
//            for(int i=1; i<result.size(); i++){
//                LocalDateTime after = result.get(i-1).getCreatedAt();
//                LocalDateTime prev = result.get(i).getCreatedAt();
//                assertThat(prev).isBeforeOrEqualTo(after);
//            }
//        }
//
//        @Test
//        @DisplayName("내가 작성한 산책로가 존재하지 않으면 빈 리스트를 반환한다.")
//        void it_returns_empty_list(){
//            // given
//            Long memberId = 2L;
//            Integer size = 5;
//            LocalDateTime lastCreateAt = LocalDateTime.now().minusSeconds(1L);
//
//            // when
//            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);
//
//            // then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("getUserWalkwayWithHashtagAndLike 메서드는")
//    class Describe_getUserWalkwayWithHashtagAndLikeEntityEntity {
//
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            em.persist(memberEntity);
//            walkwayEntity = createWalkway(memberEntity);
//            em.persist(walkwayEntity);
//        }
//
//        @Test
//        @DisplayName("해쉬태그와 좋아요와 함께 산책로를 반환한다.")
//        void it_returns_walkway_with_hashtags_liked() {
//            // Given
//            Long memberId = memberEntity.getId();
//            Long walkwayId = walkwayEntity.getId();
//
//            // When
//            WalkwayEntity result = walkwayQueryDSLRepository.getUserWalkwayWithHashtagAndLike(memberId, walkwayId);
//
//            // Then
//            assertThat(result).isEqualTo(walkwayEntity);
//            assertThat(result.getHashtagWalkways()).isNotNull();
//        }
//    }
//}