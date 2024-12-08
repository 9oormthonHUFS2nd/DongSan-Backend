package com.dongsan.domains.bookmark.service;

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
public class BookmarkCommandService {
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

    public void addWalkway(Bookmark bookmark, Walkway walkway) {
        MarkedWalkway markedWalkway = MarkedWalkway.builder()
                .bookmark(bookmark)
                .walkway(walkway)
                .build();
        markedWalkwayRepository.save(markedWalkway);
    }

    public void deleteWalkway(Bookmark bookmark, Walkway walkway) {
        markedWalkwayRepository.deleteByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId());
    }
}
