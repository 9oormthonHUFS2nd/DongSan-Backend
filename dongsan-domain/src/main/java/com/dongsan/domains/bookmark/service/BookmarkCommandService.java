package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkCommandService {
    private final BookmarkRepository bookmarkRepository;

    public Long createBookmark(Member member, String name) {
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .name(name)
                .build();
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }
}
