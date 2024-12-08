package com.dongsan.domains.bookmark.usecase;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMember;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.request.WalkwayIdRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkCommandService;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.code.BookmarkErrorCode;
import com.dongsan.error.exception.CustomException;
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
            assertEquals(BookmarkErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK, thrown.getErrorCode());
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
            assertEquals(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }
    }


}