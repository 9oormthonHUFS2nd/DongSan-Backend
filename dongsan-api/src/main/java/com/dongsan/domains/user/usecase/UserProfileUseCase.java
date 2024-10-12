package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.user.dto.response.GetBookmarksResponse;
import com.dongsan.domains.user.dto.response.GetProfileResponse;
import com.dongsan.domains.user.mapper.UserBookmarkMapper;
import com.dongsan.domains.user.mapper.UserProfileMapper;
import com.dongsan.error.code.MemberErrorCode;
import com.dongsan.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class UserProfileUseCase {

    private final MemberQueryService memberQueryService;
    private final BookmarkQueryService bookmarkQueryService;

    @Transactional(readOnly = true)
    public GetProfileResponse getUserProfile(Long userId) {

        Member member = memberQueryService.readMember(userId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        return UserProfileMapper.toGetProfileResponse(member);
    }


    @Transactional(readOnly = true)
    public GetBookmarksResponse getUserBookmarks(Long userId, Long bookmarkId, Integer limit) {

        List<Bookmark> bookmarkList = bookmarkQueryService.readUserBookmarks(bookmarkId, userId, limit);

        return UserBookmarkMapper.toGetBookmarksResponse(bookmarkList);
    }

}
