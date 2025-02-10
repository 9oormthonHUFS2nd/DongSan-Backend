//package com.dongsan.domains.bookmark.repository;
//
//import static fixture.BookmarkFixture.createBookmark;
//import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
//import static fixture.MemberFixture.createMember;
//import static fixture.WalkwayFixture.createPrivateWalkway;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
//import com.dongsan.rdb.domains.bookmark.MarkedWalkway;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
//import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("MarkedWalkwayQueryDSLRepository Unit Test")
//class MarkedWalkwayQueryDSLRepositoryTestEntity extends RepositoryTest {
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getBookmarkWalkway 메서드는")
//    class Describe_getBookmarkWalkwayEntityEntity {
//        MemberEntity memberEntity;
//        BookmarkEntity bookmarkEntity;
//
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            em.persist(memberEntity);
//            bookmarkEntity = createBookmark(memberEntity);
//            em.persist(bookmarkEntity);
//            for(int i=0; i<5; i++){
//                WalkwayEntity walkwayEntity = createWalkway(memberEntity);
//                MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
//                em.persist(walkwayEntity);
//                em.persist(markedWalkway);
//            }
//        }
//
//        @Test
//        @DisplayName("타인이 등록한 산책로의 경우 공개 상태의 산책로만 조회한다.")
//        void it_returns_others_public_walkway(){
//            // given
//            MemberEntity other = createMember();
//            WalkwayEntity otherPublicWalkwayEntity = createWalkway(other);
//            WalkwayEntity otherPrivateWalkwayEntity = createPrivateWalkway(other);
//            em.persist(other);
//            em.persist(otherPublicWalkwayEntity);
//            em.persist(otherPrivateWalkwayEntity);
//            em.persist(createMarkedWalkway(otherPrivateWalkwayEntity, bookmarkEntity));
//            em.persist(createMarkedWalkway(otherPublicWalkwayEntity, bookmarkEntity));
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 10;
//            LocalDateTime lastCreatedAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            assertThat(result).hasSize(5 + 1);
//            for(int i=0; i<result.size(); i++){
//                WalkwayEntity walkwayEntity = result.get(i).getWalkwayEntity();
//                // 타인이 등록한 산책로 일 때
//                if(!walkwayEntity.getMemberEntity().getId().equals(memberId)){
//                    assertThat(walkwayEntity.getExposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
//                }
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreatedAt이 null이 아니면 lastCreatedAt보다 작은 createdAt의 markedWalkway의 walkway를 가지고 온다.")
//        void it_returns_less_createdAt_markedWalkway(){
//            // given
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 5;
//            LocalDateTime lastCreatedAt = LocalDateTime.now().minusSeconds(1L);
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            for(int i=0; i<result.size(); i++){
//                MarkedWalkway markedWalkway = result.get(i);
//                assertThat(markedWalkway.getBookmarkEntity().getId()).isEqualTo(bookmarkId);
//                assertThat(markedWalkway.getCreatedAt()).isBefore(lastCreatedAt);
//            }
//            for(int i=0; i<result.size()-1; i++){
//                MarkedWalkway after = result.get(i);
//                MarkedWalkway prev = result.get(i+1);
//                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreatedAt이 null이면 첫 페이지를 가지고 온다.")
//        void it_returns_first_page(){
//            // given
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 3;
//            LocalDateTime lastCreatedAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            assertThat(result).hasSize(size);
//            for(int i=0; i<result.size(); i++){
//                MarkedWalkway markedWalkway = result.get(i);
//                assertThat(markedWalkway.getBookmarkEntity().getId()).isEqualTo(bookmarkId);
//            }
//            for(int i=0; i<result.size()-1; i++){
//                MarkedWalkway after = result.get(i);
//                MarkedWalkway prev = result.get(i+1);
//                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
//            }
//        }
//    }
//}