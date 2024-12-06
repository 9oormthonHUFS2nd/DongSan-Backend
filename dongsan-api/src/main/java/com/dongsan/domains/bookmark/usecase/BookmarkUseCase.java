package com.dongsan.domains.bookmark.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.request.WalkwayIdRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.mapper.BookmarkMapper;
import com.dongsan.domains.bookmark.service.BookmarkCommandService;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.code.BookmarkErrorCode;
import com.dongsan.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class BookmarkUseCase {
    private final MemberQueryService memberQueryService;
    private final BookmarkQueryService bookmarkQueryService;
    private final BookmarkCommandService bookmarkCommandService;
    private final WalkwayQueryService walkwayQueryService;


    @Transactional
    public BookmarkIdResponse createBookmark(Long memberId, BookmarkNameRequest request) {
        Member member = memberQueryService.getMember(memberId);
        // 내가 만든 북마크 중 이미 존재하는 이름인지 확인
        bookmarkQueryService.hasSameBookmarkName(member.getId(), request.name());
        Long bookmarkId = bookmarkCommandService.createBookmark(member, request.name());
        return BookmarkMapper.toBookmarkIdResponse(bookmarkId);
    }

    @Transactional
    public void renameBookmark(Long memberId, Long bookmarkId, BookmarkNameRequest request) {
        Member member = memberQueryService.getMember(memberId);
        Bookmark bookmark = bookmarkQueryService.getBookmark(bookmarkId);
        // 내 소유의 북마크인지 확인
        bookmarkQueryService.isOwnerOfBookmark(member, bookmark);
        // 이름 중복 확인
        bookmarkQueryService.hasSameBookmarkName(member.getId(), request.name());
        // 이름 변경
        bookmarkCommandService.renameBookmark(bookmark, request.name());
    }

    public void addWalkway(Long memberId, Long bookmarkId, WalkwayIdRequest request) {
        Member member = memberQueryService.getMember(memberId);
        Bookmark bookmark = bookmarkQueryService.getBookmark(bookmarkId);
        Walkway walkway = walkwayQueryService.getWalkway(request.walkwayId());
        // 내 소유의 북마크인지 화인
        bookmarkQueryService.isOwnerOfBookmark(member, bookmark);
        // 이미 추가된 산책로인지 확인
        if(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)){
            throw new CustomException(BookmarkErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK);
        } else {
            bookmarkCommandService.addWalkway(bookmark, walkway);
        }
    }
}
