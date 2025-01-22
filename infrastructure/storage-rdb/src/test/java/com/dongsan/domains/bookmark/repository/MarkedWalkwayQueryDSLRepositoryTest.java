package com.dongsan.domains.bookmark.repository;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
import static fixture.MemberFixture.createMember;
import static fixture.WalkwayFixture.createPrivateWalkway;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ExposeLevel;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("MarkedWalkwayQueryDSLRepository Unit Test")
class MarkedWalkwayQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository;

    @Nested
    @DisplayName("getBookmarkWalkway 메서드는")
    class Describe_getBookmarkWalkway{
        Member member;
        Bookmark bookmark;

        @BeforeEach
        void setUp(){
            member = createMember();
            em.persist(member);
            bookmark = createBookmark(member);
            em.persist(bookmark);
            for(int i=0; i<5; i++){
                Walkway walkway = createWalkway(member);
                MarkedWalkway markedWalkway = createMarkedWalkway(walkway, bookmark);
                em.persist(walkway);
                em.persist(markedWalkway);
            }
        }

        @Test
        @DisplayName("타인이 등록한 산책로의 경우 공개 상태의 산책로만 조회한다.")
        void it_returns_others_public_walkway(){
            // given
            Member other = createMember();
            Walkway otherPublicWalkway = createWalkway(other);
            Walkway otherPrivateWalkway = createPrivateWalkway(other);
            em.persist(other);
            em.persist(otherPublicWalkway);
            em.persist(otherPrivateWalkway);
            em.persist(createMarkedWalkway(otherPrivateWalkway, bookmark));
            em.persist(createMarkedWalkway(otherPublicWalkway, bookmark));
            Long bookmarkId = bookmark.getId();
            Integer size = 10;
            LocalDateTime lastCreatedAt = null;
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
                    memberId);

            // then
            assertThat(result).hasSize(5 + 1);
            for(int i=0; i<result.size(); i++){
                Walkway walkway = result.get(i).getWalkway();
                // 타인이 등록한 산책로 일 때
                if(!walkway.getMember().getId().equals(memberId)){
                    assertThat(walkway.getExposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
                }
            }
        }

        @Test
        @DisplayName("lastCreatedAt이 null이 아니면 lastCreatedAt보다 작은 createdAt의 markedWalkway의 walkway를 가지고 온다.")
        void it_returns_less_createdAt_markedWalkway(){
            // given
            Long bookmarkId = bookmark.getId();
            Integer size = 5;
            LocalDateTime lastCreatedAt = LocalDateTime.now().minusSeconds(1L);
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
                    memberId);

            // then
            for(int i=0; i<result.size(); i++){
                MarkedWalkway markedWalkway = result.get(i);
                assertThat(markedWalkway.getBookmark().getId()).isEqualTo(bookmarkId);
                assertThat(markedWalkway.getCreatedAt()).isBefore(lastCreatedAt);
            }
            for(int i=0; i<result.size()-1; i++){
                MarkedWalkway after = result.get(i);
                MarkedWalkway prev = result.get(i+1);
                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
            }
        }

        @Test
        @DisplayName("lastCreatedAt이 null이면 첫 페이지를 가지고 온다.")
        void it_returns_first_page(){
            // given
            Long bookmarkId = bookmark.getId();
            Integer size = 3;
            LocalDateTime lastCreatedAt = null;
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
                    memberId);

            // then
            assertThat(result).hasSize(size);
            for(int i=0; i<result.size(); i++){
                MarkedWalkway markedWalkway = result.get(i);
                assertThat(markedWalkway.getBookmark().getId()).isEqualTo(bookmarkId);
            }
            for(int i=0; i<result.size()-1; i++){
                MarkedWalkway after = result.get(i);
                MarkedWalkway prev = result.get(i+1);
                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
            }
        }
    }
}