package com.dongsan.domains.bookmark.repository;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE "
            + "(b.member = :member) AND "
            + "(:bookmarkId IS NULL OR b.id < :bookmarkId)"
            + "ORDER BY b.id DESC")
    List<Bookmark> findBookmarksByIdAndMember(Long bookmarkId, Member member, Pageable pageable);
}
