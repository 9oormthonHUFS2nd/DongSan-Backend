package com.dongsan.domains.bookmark.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.dto.param.GetBookmarkDetailParam;
import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.request.WalkwayIdRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.dto.response.GetBookmarkDetailResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.mapper.BookmarkMapper;
import com.dongsan.domains.bookmark.mapper.BookmarksWithMarkedWalkwayMapper;
import com.dongsan.domains.bookmark.service.BookmarkCommandService;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.bookmark.service.MarkedWalkwayQueryService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.common.error.code.BookmarkErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class BookmarkUseCase {
    private final MemberQueryService memberQueryService;
    private final BookmarkQueryService bookmarkQueryService;
    private final BookmarkCommandService bookmarkCommandService;
    private final WalkwayQueryService walkwayQueryService;
    private final MarkedWalkwayQueryService markedWalkwayQueryService;

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

    @Transactional
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

    @Transactional
    public void deleteWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Member member = memberQueryService.getMember(memberId);
        Bookmark bookmark = bookmarkQueryService.getBookmark(bookmarkId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
        // 내 소유의 북마크인지 확인
        bookmarkQueryService.isOwnerOfBookmark(member, bookmark);
        // 이미 추가된 산책로인지 확인
        if(bookmarkQueryService.isWalkwayAdded(bookmark, walkway)){
            bookmarkCommandService.deleteWalkway(bookmark, walkway);
        } else {
            throw new CustomException(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK);
        }
    }

    @Transactional(readOnly = true)
    public BookmarksWithMarkedWalkwayResponse getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId) {
        if (!walkwayQueryService.existsByWalkwayId(walkwayId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND);
        }
        List<BookmarksWithMarkedWalkwayDTO> bookmarks = bookmarkQueryService.getBookmarksWithMarkedWalkway(walkwayId, memberId);
        return BookmarksWithMarkedWalkwayMapper.toBookmarksWithMarkedWalkwayResponse(bookmarks);
    }

    @Transactional
    public void deleteBookmark(Long memberId, Long bookmarkId) {
        Member member = memberQueryService.getMember(memberId);
        Bookmark bookmark = bookmarkQueryService.getBookmark(bookmarkId);
        // 내 소유의 북마크인지 확인
        bookmarkQueryService.isOwnerOfBookmark(member, bookmark);
        // 삭제
        bookmarkCommandService.deleteBookmark(bookmark);
    }

    @Transactional(readOnly = true)
    public GetBookmarkDetailResponse getBookmarkDetails(GetBookmarkDetailParam param) {
        Member member = memberQueryService.getMember(param.memberId());
        Bookmark bookmark = bookmarkQueryService.getBookmark(param.bookmarkId());
        // walkwayId가 null이면 첫 페이지를 조회하는 것이다.
        Walkway walkway = param.walkwayId() == null ? null : walkwayQueryService.getWalkway(param.walkwayId());
        // 내 소유의 북마크인지 확인
        bookmarkQueryService.isOwnerOfBookmark(member, bookmark);
        // 마지막 markedBookmark의 생성시간 조회
        LocalDateTime lastCreatedAt = markedWalkwayQueryService.getCreatedAt(bookmark, walkway);
        List<Walkway> walkways = walkwayQueryService.getBookmarkWalkway(bookmark, param.size(), lastCreatedAt);
        return BookmarkMapper.toGetBookmarkDetailResponse(bookmark, walkways);
    }
}
