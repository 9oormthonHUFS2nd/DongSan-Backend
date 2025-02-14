package com.dongsan.core.domains.member;

import com.dongsan.core.domains.bookmark.BookmarkReader;
import org.springframework.stereotype.Service;

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

}
