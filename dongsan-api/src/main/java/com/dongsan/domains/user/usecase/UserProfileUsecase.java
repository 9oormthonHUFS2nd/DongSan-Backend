package com.dongsan.domains.user.usecase;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.service.BookmarkService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberService;
import com.dongsan.domains.user.dto.UserBookmarkDto;
import com.dongsan.domains.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// TODO: 마이페이지로 통합
@Service
@RequiredArgsConstructor
public class UserProfileUsecase {

    private final MemberService memberService;
    private final BookmarkService bookmarkService;

    public UserProfileDto.UserProfileRes getUserProfile(Long userId) {

        // TODO: 에러 코드 적용
        Member member = memberService.readMember(userId).orElseThrow();

        return UserProfileDto.UserProfileRes.of(member);
    }


    public UserBookmarkDto.UserBookmarksRes getUserBookmarks(Long userId, Long bookmarkId, Integer size) {

        // TODO: 에러 코드 적용
        Member member = memberService.readMember(userId).orElseThrow();

        List<Bookmark> bookmarkList = bookmarkService.readUserBookmarks(bookmarkId, member, (size == null ? 10 : size));

        return new UserBookmarkDto.UserBookmarksRes(bookmarkList.stream()
                        .map(UserBookmarkDto.UserBookmarkRes::of)
                        .collect(Collectors.toList()));
    }

}
