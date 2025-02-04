package com.dongsan.core.domains.bookmark;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.common.error.code.BookmarkErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarkedWalkwayReader {
    private final MarkedWalkwayRepository markedWalkwayRepository;

    /**
     * createdAt 조회
     * @param bookmark
     * @param walkway null이면 첫 페이지라는 뜻이므로 그대로 null 반환
     * @return
     */
    public LocalDateTime getCreatedAt(Bookmark bookmark, Walkway walkway) {
        if(walkway == null){
            return null;
        } else {
            MarkedWalkway markedWalkway = markedWalkwayRepository
                    .findByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())
                    .orElseThrow(() -> new CustomException(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK));
            return markedWalkway.getCreatedAt();
        }
    }
}
