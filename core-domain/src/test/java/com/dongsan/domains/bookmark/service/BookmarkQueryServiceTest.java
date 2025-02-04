package com.dongsan.domains.bookmark.service;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMemberWithId;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.bookmark.BookmarkQueryService;
import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.common.error.code.BookmarkErrorCode;
import com.dongsan.common.error.exception.CustomException;
import fixture.BookmarkFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkQueryService Unit Test")
class BookmarkQueryServiceTest {
    @InjectMocks
    BookmarkQueryService bookmarkQueryService;
    @Mock
    BookmarkRepository bookmarkRepository;
    @Mock
    BookmarkQueryDSLRepository bookmarkQueryDSLRepository;
    @Mock
    MarkedWalkwayRepository markedWalkwayRepository;

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

            Bookmark lastBookmark = BookmarkFixture.createBookmarkWithId(bookmarkId, null);

            when(bookmarkQueryDSLRepository.getBookmarks(lastBookmark, memberId, limit)).thenReturn(bookmarkList);

            // When
            List<Bookmark> result = bookmarkQueryService.getUserBookmarks(lastBookmark, memberId, limit);

            // Then
            assertThat(result).isNotNull().hasSize(limit);
            assertThat(result.get(0).getName()).isEqualTo(bookmarkList.get(0).getName());
            assertThat(result.get(1).getName()).isEqualTo(bookmarkList.get(1).getName());
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
            List<Bookmark> result = bookmarkQueryService.getUserBookmarks(null, memberId, limit);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasSameBookmarkName 메서드는")
    class Describe_hasSameBookmarkName{
        @Test
        @DisplayName("사용자가 저장한 북마크 중 중복되는 이름이 존재하면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Long memberId = 1L;
            String name = "북마크 이름";
            when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(true);

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                bookmarkQueryService.hasSameBookmarkName(memberId, name);
            });
            assertEquals(BookmarkErrorCode.SAME_BOOKMARK_NAME_EXIST, thrown.getErrorCode());
        }

        @Test
        @DisplayName("사용자가 저장한 북마크 중 중복되는 이름이 존재하지 않으면 예외를 반환하지 않는다.")
        void it_dont_throws_exception(){
            // given
            Long memberId = 1L;
            String name = "북마크 이름";
            when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(false);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkQueryService.hasSameBookmarkName(memberId, name);
            });
        }
    }

    @Nested
    @DisplayName("getBookmark 메서드는")
    class Describe_getBookmark{
        @Test
        @DisplayName("북마크가 존재하지 않으면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                bookmarkQueryService.getBookmark(bookmarkId);
            });
            assertEquals(BookmarkErrorCode.BOOKMARK_NOT_EXIST, thrown.getErrorCode());
        }

        @Test
        @DisplayName("북마크가 존재하면 북마크를 반환한다.")
        void it_returns_bookmark(){
            // given
            Long bookmarkId = 1L;
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", bookmarkId);
            when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

            // when
            Bookmark result = bookmarkQueryService.getBookmark(bookmarkId);

            // then
            assertThat(result.getId()).isEqualTo(bookmarkId);
        }
    }

    @Nested
    @DisplayName("isOwnerOfBookmark 메서드는")
    class Describe_isOwnerOfBookmark{
        @Test
        @DisplayName("북마크 생성자가 아니면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Member owner = createMemberWithId(1L);
            Member notOwner = createMemberWithId(2L);
            Bookmark bookmark = createBookmark(owner);

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                bookmarkQueryService.isOwnerOfBookmark(notOwner, bookmark);
            });
            assertEquals(BookmarkErrorCode.NOT_BOOKMARK_OWNER, thrown.getErrorCode());
        }

        @Test
        @DisplayName("북마크 생성자가 맞으면 예외를 반환하지 않는다.")
        void it_dont_throws_exception(){
            // given
            Member owner = createMemberWithId(1L);
            Bookmark bookmark = createBookmark(owner);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkQueryService.isOwnerOfBookmark(owner, bookmark);
            });
        }
    }

    @Nested
    @DisplayName("existsById 메서드는")
    class Describe_existsById{
        @Test
        @DisplayName("북마크가 존재하면 true를 반환한다.")
        void it_returns_true(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.existsById(bookmarkId)).thenReturn(true);

            // when
            boolean result = bookmarkQueryService.existsById(bookmarkId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("북마크가 존재하지 않으면 false를 반환한다.")
        void it_returns_false(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.existsById(bookmarkId)).thenReturn(false);

            // when
            boolean result = bookmarkQueryService.existsById(bookmarkId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("isWalkwayAdded 메서드는")
    class Describe_isWalkwayAdded {
        @Test
        @DisplayName("북마크에 포함된 산책로이면 true를 반환한다.")
        void it_returns_true() {
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", 1L);
            when(markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
                    true);

            // when
            boolean result = bookmarkQueryService.isWalkwayAdded(bookmark, walkway);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("북마크에 포함되지 않은 산책로이면 false를 반환한다.")
        void it_returns_false() {
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", 1L);
            when(markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
                    false);

            // when
            boolean result = bookmarkQueryService.isWalkwayAdded(bookmark, walkway);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getUserWalkwayBookmarks 메서드는")
    class Describe_getUserWalkwayBookmarks {
        @Test
        @DisplayName("walkwayId와 memberId가 입력 되면 Bookmark 리스트를 반환한다.")
        void it_returns_bookmarks() {
            // Given
            Long walkwayId = 1L;
            Long memberId = 1L;

            List<BookmarksWithMarkedWalkwayDTO> dto = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                dto.add(new BookmarksWithMarkedWalkwayDTO(1L, 1L, "test", 1L));
            }

            Bookmark lastBookmark = BookmarkFixture.createBookmark(null);

            when(bookmarkQueryDSLRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, lastBookmark, 5)).thenReturn(dto);

            // When
            List<BookmarksWithMarkedWalkwayDTO> result = bookmarkQueryService.getBookmarksWithMarkedWalkway(walkwayId, memberId, lastBookmark, 5);

            // Then
            assertThat(result).isEqualTo(dto);
        }
    }

}