package com.dongsan.domains.bookmark.usecase;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMember;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.common.error.code.BookmarkErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.dto.param.GetBookmarkDetailParam;
import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.request.WalkwayIdRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.dto.response.GetBookmarkDetailResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkCommandService;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.bookmark.service.MarkedWalkwayQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
    @Mock
    WalkwayQueryService walkwayQueryService;
    @Mock
    MarkedWalkwayQueryService markedWalkwayQueryService;

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
    @DisplayName("addWalkway 메서드는")
    class Describe_addWalkway{
        @Test
        @DisplayName("이미 북마크에 추가된 산책로이면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 2L;
            WalkwayIdRequest request = WalkwayIdRequest.builder()
                    .walkwayId(3L)
                    .build();
            Member member = createMember();
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);
            when(walkwayQueryService.getWalkway(request.walkwayId())).thenReturn(walkway);
            when(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)).thenReturn(true);

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                bookmarkUseCase.addWalkway(memberId, bookmarkId, request);
            });
            Assertions.assertEquals(BookmarkErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }

        @Test
        @DisplayName("북마크에 저장되지 않은 산책로이면 북마크에 산책로를 추가한다.")
        void it_adds_walkway(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 2L;
            WalkwayIdRequest request = WalkwayIdRequest.builder()
                    .walkwayId(3L)
                    .build();
            Member member = createMember();
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);
            when(walkwayQueryService.getWalkway(request.walkwayId())).thenReturn(walkway);
            when(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)).thenReturn(false);

            // when
            bookmarkUseCase.addWalkway(memberId, bookmarkId, request);

            // then
            verify(bookmarkCommandService).addWalkway(bookmark, walkway);
        }
    }

    @Nested
    @DisplayName("deleteWalkway 메서드는")
    class Describe_deleteWalkway{
        @Test
        @DisplayName("이미 북마크에 추가된 산책로이면 북마크를 삭제한다.")
        void it_deletes_walkway(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 2L;
            Long walkwayId = 3L;
            Member member = createMember();
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);
            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)).thenReturn(true);

            // when
            bookmarkUseCase.deleteWalkway(memberId, bookmarkId, walkwayId);

            // then
            verify(bookmarkCommandService).deleteWalkway(bookmark, walkway);
        }

        @Test
        @DisplayName("북마크에 저장되지 않은 산책로이면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 2L;
            Long walkwayId = 3L;
            Member member = createMember();
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);
            when(walkwayQueryService.getWalkway(walkwayId)).thenReturn(walkway);
            when(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)).thenReturn(false);

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                bookmarkUseCase.deleteWalkway(memberId, bookmarkId, walkwayId);
            });
            Assertions.assertEquals(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK, thrown.getErrorCode());
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
            int size = 5;
            List<BookmarksWithMarkedWalkwayDTO> bookmarks = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                BookmarksWithMarkedWalkwayDTO bookmark = new BookmarksWithMarkedWalkwayDTO(1L, 1L, "test", 1L);
                bookmarks.add(bookmark);
            }

            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
            when(bookmarkQueryService.getBookmarksWithMarkedWalkway(walkwayId, memberId, null, size)).thenReturn(bookmarks);

            // When
            BookmarksWithMarkedWalkwayResponse result
                    = bookmarkUseCase.getBookmarksWithMarkedWalkway(memberId, walkwayId, null, size);

            // Then
            assertThat(result.bookmarks()).hasSize(bookmarks.size());
        }
    }

    @Nested
    @DisplayName("deleteBookmark 메서드는")
    class Describe_deleteBookmark{
        @Test
        @DisplayName("북마크를 삭제한다.")
        void it_deletes_bookmark(){
            // given
            Long memberId = 1L;
            Long bookmarkId = 2L;
            Member member = createMember();
            Bookmark bookmark = createBookmark(member);
            when(memberQueryService.getMember(memberId)).thenReturn(member);
            when(bookmarkQueryService.getBookmark(bookmarkId)).thenReturn(bookmark);

            // when
            bookmarkUseCase.deleteBookmark(memberId, bookmarkId);

            // then
            verify(bookmarkQueryService).isOwnerOfBookmark(member, bookmark);
            verify(bookmarkCommandService).deleteBookmark(bookmark);
        }
    }

    @Nested
    @DisplayName("getBookmarkDetails 메서드는")
    class Describe_getBookmarkDetails{
        @Test
        @DisplayName("북마크 상세 정보를 DTO로 반환한다.")
        void it_returns_DTO(){
            // given
            GetBookmarkDetailParam param = new GetBookmarkDetailParam(1L, 2L, 10, 3L);
            Member member = createMember();
            Bookmark bookmark = createBookmark(member);
            Walkway walkway = createWalkway(member);
            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 9, 11, 11);
            List<Walkway> walkways = List.of(
                    createWalkway(null),
                    createWalkway(null));
            when(memberQueryService.getMember(param.memberId())).thenReturn(member);
            when(bookmarkQueryService.getBookmark(param.bookmarkId())).thenReturn(bookmark);
            when(walkwayQueryService.getWalkway(param.lastId())).thenReturn(walkway);
            when(markedWalkwayQueryService.getCreatedAt(bookmark, walkway)).thenReturn(lastCreatedAt);
            when(walkwayQueryService.getBookmarkWalkway(bookmark, param.size()+1, lastCreatedAt, param.memberId())).thenReturn(walkways);

            // when
            GetBookmarkDetailResponse response = bookmarkUseCase.getBookmarkDetails(param);

            // then
            assertThat(response.name()).isEqualTo(bookmark.getName());
            assertThat(response.walkways()).hasSize(walkways.size());
            for(int i=0; i<response.walkways().size(); i++){
                assertThat(response.walkways().get(i).name()).isEqualTo(walkways.get(i).getName());
            }
        }
    }
}