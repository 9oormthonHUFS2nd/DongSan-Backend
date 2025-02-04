package com.dongsan.core.domains.user;

import com.dongsan.core.domains.bookmark.BookmarkQueryService;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.bookmark.GetBookmarksResponse;
import com.dongsan.core.domains.bookmark.UserBookmarkMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UserProfileUseCase {

    private final MemberQueryService memberQueryService;
    private final BookmarkQueryService bookmarkQueryService;

    @Transactional(readOnly = true)
    public GetProfileResponse getUserProfile(Long userId) {

        Member member = memberQueryService.getMember(userId);

        return UserProfileMapper.toGetProfileResponse(member);
    }


    @Transactional(readOnly = true)
    public GetBookmarksResponse getUserBookmarks(Long userId, Long bookmarkId, Integer size) {
        Bookmark bookmark = null;
        if (bookmarkId != null) {
            bookmark = bookmarkQueryService.getBookmark(bookmarkId);
        }

        List<Bookmark> bookmarkList = bookmarkQueryService.getUserBookmarks(bookmark, userId, size);

        return UserBookmarkMapper.toGetBookmarksResponse(bookmarkList, size);
    }

}
