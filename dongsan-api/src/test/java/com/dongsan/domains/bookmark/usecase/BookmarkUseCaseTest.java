package com.dongsan.domains.bookmark.usecase;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMember;
import static fixture.ReflectFixture.reflectField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.service.BookmarkCommandService;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import fixture.BookmarkFixture;
import fixture.MarkedWalkwayFixture;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkUseCase Unit Test")
class BookmarkUseCaseTest {
    @InjectMocks
    BookmarkUseCase bookmarkUseCase;
    @Mock
    MemberQueryService memberQueryService;
    @Mock
    BookmarkQueryService bookmarkQueryService;
    @Mock
    BookmarkCommandService bookmarkCommandService;

    @Nested
    @DisplayName("createBookmark 메서드는")
    class Describe_createBookmark{
        @Test
        @DisplayName("생성한 북마크의 id를 DTO로 변환한다.")
        void it_returns_responseDTO(){
            // given
            Long memberId = 1L;
            Member member = createMember();
            reflectField(member, "id", memberId);
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name("북마크1")
                    .build();
            Long bookmarkId = 1L;
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkCommandService.createBookmark(member, request.name())).thenReturn(bookmarkId);

            // when
            BookmarkIdResponse response = bookmarkUseCase.createBookmark(memberId, request);

            // then
            verify(bookmarkQueryService).hasSameBookmarkName(member.getId(), request.name());
            assertThat(response.bookmarkId()).isEqualTo(bookmarkId);
        }
    }


    @Nested
    @DisplayName("renameBookmark 메서드는")
    class Describe_renameBookmark{
        @Test
        @DisplayName("북마크 이름을 변경한다.")
        void it_renames(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 1L;
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name("북마크1")
                    .build();
            Member member = createMember();
            reflectField(member, "id", memberId);
            Bookmark bookmark = createBookmark(member);
            reflectField(bookmark, "id", bookmarkId);

            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);

            // when
            bookmarkUseCase.renameBookmark(memberId, bookmarkId, request);

            // then
            verify(bookmarkQueryService).isOwnerOfBookmark(member, bookmark);
            verify(bookmarkQueryService).hasSameBookmarkName(member.getId(), request.name());
            verify(bookmarkCommandService).renameBookmark(bookmark, request.name());
        }
    }

    @Nested
    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
    class Describe_getBookmarksWithMarkedWalkway {
        @Test
        @DisplayName("북마크 리스트 DTO를 반환한다.")
        void it_returns_DTO() {
            // Given
            Long memberId = 1L;
            Long walkwayId = 1L;
            List<Bookmark> bookmarks = new ArrayList<>();

            for(int i = 0; i < 5; i++) {
                Bookmark bookmark = BookmarkFixture.createBookmark(null);
                MarkedWalkway markedWalkway = MarkedWalkwayFixture.createMarkedWalkway(null, bookmark);
                bookmark.addMarkedWalkway(markedWalkway);
                bookmarks.add(bookmark);
            }

            when(bookmarkQueryService.getBookmarksWithMarkedWalkway(walkwayId, memberId)).thenReturn(bookmarks);

            // When
            BookmarksWithMarkedWalkwayResponse result = bookmarkUseCase.getBookmarksWithMarkedWalkway(memberId, walkwayId);

            // Then
            assertThat(result.bookmarks()).hasSize(bookmarks.size());
        }
    }
}