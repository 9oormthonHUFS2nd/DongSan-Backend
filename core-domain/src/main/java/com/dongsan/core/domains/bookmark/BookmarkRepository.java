package com.dongsan.core.domains.bookmark;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository {
    Optional<Bookmark> findById(Long bookmarkId);

    Long save(Long memberId, String name);

    boolean existsById(Long bookmarkId);

    boolean existsByMemberIdAndName(Long memberId, String name);

    void rename(Long bookmarkId, String name);

    boolean isWalkwayAdded(Long bookmarkId, Long walkwayId);

    void includeWalkway(Long bookmarkId, Long walkwayId);

    void excludeWalkway(Long bookmarkId, Long walkwayId);

    void deleteById(Long bookmarkId);
}
