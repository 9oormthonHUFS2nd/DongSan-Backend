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

import java.util.Arrays;
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
    void UserProfileUsecase_getUserProfile_ReturnUserProfileRes() {
        // Given
        Long memberId = 1L;

        Member member = Member.builder()
                .profileImageUrl("Test Url")
                .nickname("Test Nickname")
                .email("test@gmail.com")
                .build();

        UserProfileDto.UserProfileRes userProfileRes = UserProfileDto.UserProfileRes.of(member);

        when(memberService.readMember(memberId)).thenReturn(Optional.ofNullable(member));

        // When
        UserProfileDto.UserProfileRes userProfileResReturn = userProfileUsecase.getUserProfile(memberId);

        // Then
        Assertions.assertThat(userProfileResReturn)
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

        Bookmark bookmark1 = Bookmark.builder()
                .name("test1")
                .build();

        Bookmark bookmark2 = Bookmark.builder()
                .name("test2")
                .build();

        List<Bookmark> bookmarkList = Arrays.asList(bookmark2, bookmark1);

        when(memberService.readMember(userId)).thenReturn(Optional.of(member));
        when(bookmarkService.readUserBookmarks(bookmarkId, member, size)).thenReturn(bookmarkList);

        // When
        UserBookmarkDto.UserBookmarksRes userBookmarksResReturn =
                userProfileUsecase.getUserBookmarks(userId, bookmarkId, size);

        // Then
        Assertions.assertThat(userBookmarksResReturn.bookmarks().size()).isEqualTo(size);
        Assertions.assertThat(userBookmarksResReturn.bookmarks().get(0).title()).isEqualTo(bookmark2.getName());
        Assertions.assertThat(userBookmarksResReturn.bookmarks().get(1).title()).isEqualTo(bookmark1.getName());

    }
}