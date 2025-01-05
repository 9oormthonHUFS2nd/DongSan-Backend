package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.user.response.GetBookmarksResponse;
import com.dongsan.domains.user.response.GetProfileResponse;
import com.dongsan.domains.user.mapper.UserBookmarkMapper;
import com.dongsan.domains.user.mapper.UserProfileMapper;
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
    public GetBookmarksResponse getUserBookmarks(Long userId, Long bookmarkId, Integer limit) {

        List<Bookmark> bookmarkList = bookmarkQueryService.getUserBookmarks(bookmarkId, userId, limit);

        return UserBookmarkMapper.toGetBookmarksResponse(bookmarkList);
    }

}
