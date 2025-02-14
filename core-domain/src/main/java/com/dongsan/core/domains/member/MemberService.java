package com.dongsan.core.domains.member;

import com.dongsan.core.domains.bookmark.BookmarkReader;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.core.domains.bookmark.GetBookmarksResponse;
import com.dongsan.core.domains.bookmark.UserBookmarkMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberReader memberReader;
    private final BookmarkReader bookmarkReader;

    public MemberService(MemberReader memberReader, BookmarkReader bookmarkReader) {
        this.memberReader = memberReader;
        this.bookmarkReader = bookmarkReader;
    }

    public Member getMember(Long memberId) {
        return memberReader.readMember(memberId);
    }


    @Transactional(readOnly = true)
    public GetBookmarksResponse getUserBookmarks(Long userId, Long bookmarkId, Integer size) {
        Bookmark bookmark = null;
        if (bookmarkId != null) {
            bookmark = bookmarkReader.getBookmark(bookmarkId);
        }

        List<Bookmark> bookmarkList = bookmarkReader.getUserBookmarks(bookmark, userId, size);

        return UserBookmarkMapper.toGetBookmarksResponse(bookmarkList, size);
    }

}
