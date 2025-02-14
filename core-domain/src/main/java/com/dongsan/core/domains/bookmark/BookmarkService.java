package com.dongsan.core.domains.bookmark;

import com.dongsan.core.domains.common.CursorPagingRequest;
import com.dongsan.core.domains.common.CursorPagingResponse;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {
    private final BookmarkReader bookmarkReader;
    private final BookmarkWriter bookmarkWriter;
    private final BookmarkValidator bookmarkValidator;
    private final WalkwayReader walkwayReader;

    public BookmarkService(BookmarkReader bookmarkReader, BookmarkWriter bookmarkWriter,
                           BookmarkValidator bookmarkValidator, WalkwayReader walkwayReader) {
        this.bookmarkReader = bookmarkReader;
        this.bookmarkWriter = bookmarkWriter;
        this.bookmarkValidator = bookmarkValidator;
        this.walkwayReader = walkwayReader;
    }

    @Transactional
    public Long createBookmark(Long memberId, String name) {
        bookmarkValidator.validateUniqueBookmarkName(memberId, name);
        return bookmarkWriter.createBookmark(memberId, name);
    }

    @Transactional
    public void renameBookmark(Long memberId, Long bookmarkId, String name) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        bookmarkValidator.validateUniqueBookmarkName(memberId, name);
        bookmarkWriter.renameBookmark(bookmark, name);
    }

    @Transactional
    public void includeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        bookmarkValidator.validateWalkwayNotInBookmark(bookmarkId, walkwayId);
        bookmarkWriter.includeWalkway(bookmarkId, walkwayId);
    }

    @Transactional
    public void excludeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        bookmarkValidator.validateWalkwayExistsInBookmark(bookmarkId, walkwayId);
        bookmarkWriter.excludeWalkway(bookmarkId, walkwayId);
    }

    @Transactional
    public void deleteBookmark(Long memberId, Long bookmarkId) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        bookmarkWriter.deleteBookmark(bookmarkId);
    }

    public CursorPagingResponse<Walkway> getBookmarkDetails(Long memberId, Long bookmarkId, CursorPagingRequest cursorPagingRequest) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        // walkwayId가 null이면 첫 페이지를 조회하는 것이다.
        Walkway walkway = cursorPagingRequest.lastId() == null ? null : walkwayReader.getWalkway(cursorPagingRequest.lastId());
        // 마지막 markedBookmark의 생성시간 조회
        LocalDateTime lastCreatedAt = .getCreatedAt(bookmark, walkway);
        List<Walkway> walkways = walkwayQueryService.getBookmarkWalkway(bookmark, cursorPagingRequest.size()+1, lastCreatedAt, memberId);
        return CursorPagingResponse.from(walkways, cursorPagingRequest.size());
    }

    public GetBookmarksResponse getUserBookmarks(Long userId, CursorPagingRequest paging) {
        Bookmark bookmark = null;
        if (paging.lastId() != null) {
            bookmark = bookmarkReader.getBookmark(paging.lastId());
        }

        List<Bookmark> bookmarks = bookmarkReader.getUserBookmarks(bookmark, userId, paging.size());

        return UserBookmarkMapper.toGetBookmarksResponse(bookmarkList, size);
    }

    public BookmarksWithMarkedWalkwayResponse getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId, Long lastId, Integer size) {
        if (!walkwayQueryService.existsWalkway(walkwayId)) {
            throw new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND);
        }

        Bookmark bookmark = null;
        if (lastId != null) {
            bookmark = bookmarkReader.getBookmark(lastId);
        }

        List<BookmarksWithMarkedWalkwayDTO> bookmarks = bookmarkReader.getBookmarksWithMarkedWalkway(walkwayId, memberId, bookmark, size);
        return BookmarksWithMarkedWalkwayMapper.toBookmarksWithMarkedWalkwayResponse(bookmarks, size);
    }


}
