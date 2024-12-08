package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.error.code.BookmarkErrorCode;
import com.dongsan.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkQueryService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryDSLRepository bookmarkQueryDSLRepository;
    private final MarkedWalkwayRepository markedWalkwayRepository;

    public List<Bookmark> getUserBookmarks(Long bookmarkId, Long memberId, Integer limit) {
        return bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);
    }

    public void hasSameBookmarkName(Long memberId, String name){
        boolean exist = bookmarkRepository.existsByMemberIdAndName(memberId, name);
        if(exist){
            throw new CustomException(BookmarkErrorCode.SAME_BOOKMARK_NAME_EXIST);
        }
    }

    public Bookmark getBookmark(Long bookmarkId){
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
                () -> new CustomException(BookmarkErrorCode.BOOKMARK_NOT_EXIST)
        );
    }

    public void isOwnerOfBookmark(Member member, Bookmark bookmark) {
        // 북마크 생성자가 아니면
        if(!bookmark.getMember().getId().equals(member.getId())){
            throw new CustomException(BookmarkErrorCode.NOT_BOOKMARK_OWNER);
        }
    }

    public boolean existsById(Long bookmarkId){
        return bookmarkRepository.existsById(bookmarkId);
    }

    /**
     * 북마크에 포함된 산책로인지 확인
     */
    public boolean isWalkwayAdded(Bookmark bookmark, Walkway walkway) {
        return markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId());
    }

    public List<BookmarksWithMarkedWalkwayDTO> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId) {
        return bookmarkQueryDSLRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId);
    }

}
