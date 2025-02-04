package com.dongsan.core.domains.bookmark;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.Walkway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkWriter {
    private final BookmarkRepository bookmarkRepository;
    private final MarkedWalkwayRepository markedWalkwayRepository;

    public Long createBookmark(Member member, String name) {
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .name(name)
                .build();
        return bookmarkRepository.save(bookmark).getId();
    }

    public void renameBookmark(Bookmark bookmark, String name) {
        bookmark.rename(name);
    }

    /**
     * 북마크에 산책로 추가
     */
    public void addWalkway(Bookmark bookmark, Walkway walkway) {
        MarkedWalkway markedWalkway = MarkedWalkway.builder()
                .bookmark(bookmark)
                .walkway(walkway)
                .build();
        markedWalkwayRepository.save(markedWalkway);
    }

    /**
     * 북마크에서 산책로 제거
     */
    public void deleteWalkway(Bookmark bookmark, Walkway walkway) {
        markedWalkwayRepository.deleteByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId());
    }

    /**
     * Bookmark & MarkedWalkway 삭제
     */
    public void deleteBookmark(Bookmark bookmark) {
        bookmarkRepository.deleteById(bookmark.getId());
        markedWalkwayRepository.deleteAllByBookmarkId(bookmark.getId());
    }
}
