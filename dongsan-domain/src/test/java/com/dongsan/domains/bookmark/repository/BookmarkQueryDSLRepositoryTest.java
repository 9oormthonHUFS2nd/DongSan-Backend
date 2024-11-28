package com.dongsan.domains.bookmark.repository;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookmarkQueryDSLRepository Unit Test")
class BookmarkQueryDSLRepositoryTest extends RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookmarkQueryDSLRepository bookmarkQueryDSLRepository;

    Member member1;
    Member member2;
    List<Bookmark> bookmarkList = new ArrayList<>();

    @BeforeEach
    void setUpData(){
        member1 = createMember();
        member2 = createMember();
        entityManager.persist(member1);
        entityManager.persist(member2);
        for(long i = 0; i < 10; i++) {
            Bookmark bookmark = createBookmark(member1, "test"+i);
            bookmarkList.add(bookmark);
            entityManager.persist(bookmark);
        }
    }

    @Nested
    @DisplayName("getBookmarks 메서드는")
    class Describe_getBookmarks {

        @Test
        @DisplayName("bookmarkId에 값이 있으면 bookmarkId보다 낮은 아이디의 북마크를 limit만큼 내림차순으로 반환한다.")
        void it_returns_after_bookmarkId_bookmarks() {
            // Given
            Integer limit = 3;
            Long bookmarkId = bookmarkList.get(5).getId();
            Long memberId = member1.getId();

            // When
            List<Bookmark> result = bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);

            // Then
            assertThat(result.size()).isEqualTo(limit);
            for(int i = 0; i < limit; i++) {
                assertThat(result.get(i).getId()).isLessThan(bookmarkId);
                if (i < limit - 1) {
                    assertThat(result.get(i).getId()).isGreaterThan(result.get(i + 1).getId());
                }
            }
        }

        @Test
        @DisplayName("bookmarkId에 값이 null이면 첫 페이지의 북마크를 limit만큼 내림차순으로 반환한다.")
        void it_returns_first_page_bookmarks() {
            // Given
            Integer limit = 3;
            Long bookmarkId = null;
            Long memberId = member1.getId();

            // When
            List<Bookmark> result = bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);

            // Then
            assertThat(result.size()).isEqualTo(limit);
            for(int i = 0; i < limit; i++) {
                if (i < limit - 1) {
                    assertThat(result.get(i).getId()).isGreaterThan(result.get(i + 1).getId());
                }
            }
        }

        @Test
        @DisplayName("북마크가 없으면 빈 리스트를 반환한다.")
        void it_returns_empty_list() {
            // Given
            Integer limit = 3;
            Long bookmarkId = null;
            Long memberId = member2.getId();

            // When
            List<Bookmark> result = bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);

            // Then
            assertThat(result).isEmpty();
        }
    }

}