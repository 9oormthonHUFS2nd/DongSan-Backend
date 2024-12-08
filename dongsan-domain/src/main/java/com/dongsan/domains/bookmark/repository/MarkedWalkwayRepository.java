package com.dongsan.domains.bookmark.repository;

import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkedWalkwayRepository extends JpaRepository<MarkedWalkway, Long> {
    boolean existsByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);
    void deleteByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);
}
