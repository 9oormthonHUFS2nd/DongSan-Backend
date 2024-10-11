package com.dongsan.domains.user.usecase;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberService;
import com.dongsan.domains.user.dto.UserBookmarkDto;
import com.dongsan.domains.user.dto.UserProfileDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileUsecaseTest {

    @Mock
    private MemberService memberService;

    @Mock
    private BookmarkService bookmarkService;

    @InjectMocks
    private UserProfileUsecase userProfileUsecase;


    @Test
    @DisplayName("마이페이지 유저 프로필 조회")
    void getUserProfile() {
        // Given
        Long memberId = 1L;

        Member member = Member.builder()
                .profileImageUrl("Test Url")
                .nickname("Test Nickname")
                .email("test@gmail.com")
                .build();

        UserProfileDto.UserProfileRes userProfileRes = UserProfileDto.UserProfileRes.of(member);

        when(memberService.readMember(memberId)).thenReturn(Optional.of(member));

        // When
        UserProfileDto.UserProfileRes profileReturn = userProfileUsecase.getUserProfile(memberId);

        // Then
        Assertions.assertThat(profileReturn)
                .isNotNull()
                .isEqualTo(userProfileRes);
    }

    @Test
    @DisplayName("마이페이지 유저 북마크 리스트")
    void getUserBookmarks() {

        // Given
        Long userId = 1L;
        Long bookmarkId = 3L;
        Integer size = 2;

        Member member = Member.builder().build();

        List<Bookmark> bookmarkList = new ArrayList<>();

        for(long id = 2L; id != 0L; id--) {
            Bookmark bookmark = Bookmark.builder()
                    .name("test"+id)
                    .build();

            bookmarkList.add(bookmark);
        }

        when(memberService.readMember(userId)).thenReturn(Optional.of(member));
        when(bookmarkService.readUserBookmarks(bookmarkId, member, size)).thenReturn(bookmarkList);

        // When
        UserBookmarkDto.UserBookmarksRes bookmarksReturn =
                userProfileUsecase.getUserBookmarks(userId, bookmarkId, size);

        // Then
        Assertions.assertThat(bookmarksReturn.bookmarks().size()).isEqualTo(size);
        Assertions.assertThat(bookmarksReturn.bookmarks().get(0).title()).isEqualTo(bookmarkList.get(0).getName());
        Assertions.assertThat(bookmarksReturn.bookmarks().get(1).title()).isEqualTo(bookmarkList.get(1).getName());

    }
}