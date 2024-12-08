package com.dongsan.domains.bookmark.service;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
import static fixture.MemberFixture.createMemberWithId;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkCommandService Unit Test")
class BookmarkCommandServiceTest {
    @InjectMocks
    BookmarkCommandService bookmarkCommandService;
    @Mock
    BookmarkRepository bookmarkRepository;
    @Mock
    MarkedWalkwayRepository markedWalkwayRepository;

    @Nested
    @DisplayName("createBookmark 메서드는")
    class Describe_createBookmark{
        @Test
        @DisplayName("북마크를 생성한다.")
        void it_creates_bookmark(){
            // given
            Member member = createMemberWithId(1L);
            String name = "생성할 북마크 이름";
            Bookmark bookmark = createBookmark(member);
            reflectField(bookmark, "id", 1L);
            when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

            // when
            Long result = bookmarkCommandService.createBookmark(member, name);

            // then
            verify(bookmarkRepository).save(any(Bookmark.class));
            assertThat(result).isEqualTo(bookmark.getId());
        }
    }

    @Nested
    @DisplayName("renameBookmark 메서드는")
    class Describe_renameBookmark{
        @Test
        @DisplayName("북마크의 이름을 변경한다.")
        void it_renames(){
            // given
            Bookmark bookmark = createBookmark(null);
            String name = "수정된 북마크 이름";

            // when
            bookmarkCommandService.renameBookmark(bookmark, name);

            // then
            assertThat(bookmark.getName()).isEqualTo(name);
        }
    }

    @Nested
    @DisplayName("addWalkway 메서드는")
    class Describe_addWalkway{
        @Test
        @DisplayName("북마크에 산책로를 추가한다.")
        void it_adds_walkway(){
            // given
            Bookmark bookmark = createBookmark(null);
            Walkway walkway = createWalkway(null);
            MarkedWalkway markedWalkway = createMarkedWalkway(bookmark, walkway);
            when(markedWalkwayRepository.save(any(MarkedWalkway.class))).thenReturn(markedWalkway);

            // when
            bookmarkCommandService.addWalkway(bookmark, walkway);

            // then
            verify(markedWalkwayRepository).save(any(MarkedWalkway.class));
        }
    }

    @Nested
    @DisplayName("deleteWalkway 메서드는")
    class Describe_deleteWalkway{
        @Test
        @DisplayName("북마크에 산책로를 삭제한다.")
        void it_deletes_walkway(){
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", 1L);
            doNothing().when(markedWalkwayRepository).deleteByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId());


            // when
            bookmarkCommandService.deleteWalkway(bookmark, walkway);

            // then
            verify(markedWalkwayRepository).deleteByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId());
        }
    }
}