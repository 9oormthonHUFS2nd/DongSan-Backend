package com.dongsan.domains.user.usecase;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.user.mapper.UserProfileMapper;
import com.dongsan.domains.user.response.GetBookmarksResponse;
import com.dongsan.domains.user.response.GetProfileResponse;
import fixture.MemberFixture;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileUseCase Unit Test")
class UserProfileUseCaseTest {

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private BookmarkQueryService bookmarkQueryService;

    @InjectMocks
    private UserProfileUseCase userProfileUsecase;

    @Nested
    @DisplayName("getUserProfile 메서드는")
    class Describe_getUserProfile {
        @Test
        @DisplayName("유저가 존재하면 유저 프로필을 DTO로 반환한다.")
        void it_returns_responseDTO() {
            // Given
            Long memberId = 1L;

            Member member = createMemberWithId(memberId);

            GetProfileResponse getProfileResponse = UserProfileMapper.toGetProfileResponse(member);

            when(memberQueryService.getMember(memberId)).thenReturn(member);

            // When
            GetProfileResponse result = userProfileUsecase.getUserProfile(memberId);

            // Then
            Assertions.assertThat(result)
                    .isNotNull()
                    .isEqualTo(getProfileResponse);
        }
    }


    @Nested
    @DisplayName("getUserBookmarks 메서드는")
    class Describe_getUserBookmarks {
        @Test
        @DisplayName("북마크가 존재하면 북마크 리스트를 DTO로 반환한다.")
        void getUserBookmarks() {

            // Given
            Long userId = 1L;
            Long bookmarkId = 3L;
            Integer limit = 2;

            Member member = MemberFixture.createMember();

            List<Bookmark> bookmarkList = new ArrayList<>();

            for(long id = 2L; id != 0L; id--) {
                Bookmark bookmark = createBookmark(member, "test"+id);

                bookmarkList.add(bookmark);
            }

            when(bookmarkQueryService.getUserBookmarks(bookmarkId, userId, limit)).thenReturn(bookmarkList);

            // When
            GetBookmarksResponse result =
                    userProfileUsecase.getUserBookmarks(userId, bookmarkId, limit);

            // Then
            Assertions.assertThat(result.bookmarks().size()).isEqualTo(limit);
            Assertions.assertThat(result.bookmarks().get(0).title()).isEqualTo(bookmarkList.get(0).getName());
            Assertions.assertThat(result.bookmarks().get(1).title()).isEqualTo(bookmarkList.get(1).getName());

        }

    }
}