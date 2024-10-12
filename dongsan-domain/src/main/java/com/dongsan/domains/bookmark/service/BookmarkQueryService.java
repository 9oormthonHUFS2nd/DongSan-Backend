package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {

    private final BookmarkQueryDSLRepository bookmarkQueryDSLRepository;

    @Transactional(readOnly = true)
    public List<Bookmark> readUserBookmarks(Long bookmarkId, Long memberId, Integer limit) {
        return bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);
    }
}
