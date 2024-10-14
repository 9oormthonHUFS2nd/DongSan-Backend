package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkQueryServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private BookmarkQueryDSLRepository bookmarkQueryDSLRepository;

    @InjectMocks
    private BookmarkQueryService bookmarkQueryService;

    @Nested
    @DisplayName("readUserBookmarks 메서드는")
    class Describe_readUserBookmarks {
        @Test
        @DisplayName("북마크가 존재하면 북마크 리스트를 반환한다.")
        void it_returns_bookmark_list() {

            // Given
            Long memberId = 1L;
            Long bookmarkId = 3L;
            Integer limit = 2;

            List<Bookmark> bookmarkList = new ArrayList<>();
            Member member = createMemberWithId(memberId);

            for (long id = 2L; id != 0; id--) {
                Bookmark bookmark = createBookmark(member, "test" + id);
                bookmarkList.add(bookmark);
            }

            when(bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit)).thenReturn(bookmarkList);

            // When
            List<Bookmark> result = bookmarkQueryService.readUserBookmarks(bookmarkId, memberId, limit);

            // Then
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.size()).isEqualTo(limit);
            Assertions.assertThat(result.get(0).getName()).isEqualTo(bookmarkList.get(0).getName());
            Assertions.assertThat(result.get(1).getName()).isEqualTo(bookmarkList.get(1).getName());
        }

        @Test
        @DisplayName("북마크가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list() {

            // Given
            Long memberId = 1L;
            Integer limit = 10;

            List<Bookmark> bookmarkList = new ArrayList<>();

            when(bookmarkQueryDSLRepository.getBookmarks(null, memberId, limit)).thenReturn(bookmarkList);

            // When
            List<Bookmark> result = bookmarkQueryService.readUserBookmarks(null, memberId, limit);

            // Then
            Assertions.assertThat(result).isEmpty();
        }
    }

}