package com.dongsan.core.domains.bookmark;

import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.common.error.code.BookmarkErrorCode;
import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class BookmarkService {
    private final MemberReader memberReader;
    private final BookmarkReader bookmarkReader;
    private final BookmarkWriter bookmarkWriter;
    private final WalkwayReader walkwayQueryService;
    private final MarkedWalkwayReader markedWalkwayReader;

    @Transactional
    public BookmarkIdResponse createBookmark(Long memberId, BookmarkNameRequest request) {
        Member member = memberReader.getMember(memberId);
        // 내가 만든 북마크 중 이미 존재하는 이름인지 확인
        bookmarkReader.hasSameBookmarkName(member.getId(), request.name());
        Long bookmarkId = bookmarkWriter.createBookmark(member, request.name());
        return BookmarkMapper.toBookmarkIdResponse(bookmarkId);
    }

    @Transactional
    public void renameBookmark(Long memberId, Long bookmarkId, BookmarkNameRequest request) {
        Member member = memberReader.getMember(memberId);
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        // 내 소유의 북마크인지 확인
        bookmarkReader.isOwnerOfBookmark(member, bookmark);
        // 이름 중복 확인
        bookmarkReader.hasSameBookmarkName(member.getId(), request.name());
        // 이름 변경
        bookmarkWriter.renameBookmark(bookmark, request.name());
    }

    @Transactional
    public void addWalkway(Long memberId, Long bookmarkId, WalkwayIdRequest request) {
        Member member = memberReader.getMember(memberId);
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        Walkway walkway = walkwayQueryService.getWalkway(request.walkwayId());
        // 내 소유의 북마크인지 화인
        bookmarkReader.isOwnerOfBookmark(member, bookmark);
        // 이미 추가된 산책로인지 확인
        if(bookmarkReader.isWalkwayAdded(bookmark, walkway)){
            throw new CustomException(BookmarkErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK);
        } else {
            bookmarkWriter.addWalkway(bookmark, walkway);
        }
    }

    @Transactional
    public void deleteWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Member member = memberReader.getMember(memberId);
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
        // 내 소유의 북마크인지 확인
        bookmarkReader.isOwnerOfBookmark(member, bookmark);
        // 이미 추가된 산책로인지 확인
        if(bookmarkReader.isWalkwayAdded(bookmark, walkway)){
            bookmarkWriter.deleteWalkway(bookmark, walkway);
        } else {
            throw new CustomException(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK);
        }
    }

    @Transactional(readOnly = true)
    public BookmarksWithMarkedWalkwayResponse getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId, Long lastId, Integer size) {
        if (!walkwayQueryService.existsByWalkwayId(walkwayId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND);
        }

        Bookmark bookmark = null;
        if (lastId != null) {
            bookmark = bookmarkReader.getBookmark(lastId);
        }

        List<BookmarksWithMarkedWalkwayDTO> bookmarks = bookmarkReader.getBookmarksWithMarkedWalkway(walkwayId, memberId, bookmark, size);
        return BookmarksWithMarkedWalkwayMapper.toBookmarksWithMarkedWalkwayResponse(bookmarks, size);
    }

    @Transactional
    public void deleteBookmark(Long memberId, Long bookmarkId) {
        Member member = memberReader.getMember(memberId);
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        // 내 소유의 북마크인지 확인
        bookmarkReader.isOwnerOfBookmark(member, bookmark);
        // 삭제
        bookmarkWriter.deleteBookmark(bookmark);
    }

    @Transactional(readOnly = true)
    public GetBookmarkDetailResponse getBookmarkDetails(GetBookmarkDetailParam param) {
        Member member = memberReader.getMember(param.memberId());
        Bookmark bookmark = bookmarkReader.getBookmark(param.bookmarkId());
        // 내 소유의 북마크인지 확인
        bookmarkReader.isOwnerOfBookmark(member, bookmark);
        // walkwayId가 null이면 첫 페이지를 조회하는 것이다.
        Walkway walkway = param.lastId() == null ? null : walkwayQueryService.getWalkway(param.lastId());
        // 마지막 markedBookmark의 생성시간 조회
        LocalDateTime lastCreatedAt = markedWalkwayReader.getCreatedAt(bookmark, walkway);
        List<Walkway> walkways = walkwayQueryService.getBookmarkWalkway(bookmark, param.size()+1, lastCreatedAt, param.memberId());
        // hasNext 계산
        boolean hasNext = walkways.size() > param.size();
        if(hasNext){
            walkways.remove(walkways.size()-1);
        }
        return new GetBookmarkDetailResponse(bookmark, walkways, hasNext);
    }
}
