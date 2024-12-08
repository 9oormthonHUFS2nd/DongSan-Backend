package com.dongsan.domains.bookmark.repository;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("MarkedWalkwayRepository Unit Test")
class MarkedWalkwayRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private MarkedWalkwayRepository markedWalkwayRepository;

    @Nested
    @DisplayName("existsByBookmarkIdAndWalkwayId 메서드는")
    class Describe_existsByBookmarkIdAndWalkwayId{
        @BeforeEach
        void setUp(){
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            MarkedWalkway markedWalkway = createMarkedWalkway(bookmark, walkway);
            em.persist(bookmark);
            em.persist(walkway);
            em.persist(markedWalkway);
        }

        @Test
        @DisplayName("북마크에 산책로가 존재하면 true를 반환한다.")
        void it_returns_true(){
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 1L;

            // when
            boolean result = markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("북마크에 산책로가 존재하지 않으면 false를 반환한다.")
        void it_returns_false(){
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 999L;

            // when
            boolean result = markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("deleteByBookmarkIdAndWalkwayId 메소드는")
    class Describe_deleteByBookmarkIdAndWalkwayId{
        @BeforeEach
        void setUp(){
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            MarkedWalkway markedWalkway = createMarkedWalkway(bookmark, walkway);
            em.persist(bookmark);
            em.persist(walkway);
            em.persist(markedWalkway);
        }

        @Test
        @DisplayName("markedWalkway를 삭제한다.")
        void it_deletes_markedWalkway(){
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 1L;

            // when
            markedWalkwayRepository.deleteByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);

            // then
            boolean result = markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
            assertThat(result).isFalse();
        }
    }

}