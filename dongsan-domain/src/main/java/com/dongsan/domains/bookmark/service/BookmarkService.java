package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public List<Bookmark> readUserBookmarks(Long bookmarkId, Member member, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        return bookmarkRepository.findBookmarksByIdAndMember(bookmarkId, member, pageable);
    }
}
