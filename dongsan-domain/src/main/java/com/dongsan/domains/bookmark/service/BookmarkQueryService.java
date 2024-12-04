package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
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

    public List<Bookmark> readUserBookmarks(Long bookmarkId, Long memberId, Integer limit) {
        return bookmarkQueryDSLRepository.getBookmarks(bookmarkId, memberId, limit);
    }

    public void checkSameBookmarkName(Long memberId, String name){
        boolean exist = bookmarkRepository.existsByMemberIdAndName(memberId, name);
        if(exist){
            throw new CustomException(BookmarkErrorCode.SAME_BOOKMARK_NAME_EXIST);
        }
    }


}
