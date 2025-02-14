package com.dongsan.rdb.domains.bookmark;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkRepository;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.rdb.domains.bookmark.entity.QBookmark;
import com.dongsan.rdb.domains.bookmark.entity.QMarkedWalkway;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.repository.WalkwayJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkCoreRepository implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final MarkedWalkwayJpaRepository markedWalkwayJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;
    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;


    public BookmarkCoreRepository(BookmarkJpaRepository bookmarkJpaRepository,
                                  MarkedWalkwayJpaRepository markedWalkwayJpaRepository, MemberJpaRepository memberJpaRepository,
                                  WalkwayJpaRepository walkwayJpaRepository, JPAQueryFactory queryFactory) {
        this.bookmarkJpaRepository = bookmarkJpaRepository;
        this.markedWalkwayJpaRepository = markedWalkwayJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Bookmark> findById(Long bookmarkId) {
        return bookmarkJpaRepository.findById(bookmarkId)
                .map(BookmarkEntity::toBookmark);
    }

    @Override
    public boolean existsByMemberIdAndName(Long memberId, String name) {
        return bookmarkJpaRepository.existsByMemberIdAndName(memberId, name);
    }

    @Override
    public void rename(Long bookmarkId, String name) {
        BookmarkEntity bookmarkEntity = bookmarkJpaRepository.getReferenceById(bookmarkId);
        bookmarkEntity.rename(name);
    }

    @Override
    public boolean isWalkwayAdded(Long bookmarkId, Long walkwayId) {
        return markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }

    @Override
    public void includeWalkway(Long bookmarkId, Long walkwayId) {
        BookmarkEntity bookmarkEntity = bookmarkJpaRepository.getReferenceById(bookmarkId);
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(walkwayId);
        MarkedWalkway markedWalkway = new MarkedWalkway(bookmarkEntity, walkwayEntity);
        markedWalkwayJpaRepository.save(markedWalkway);
    }

    @Override
    public void excludeWalkway(Long bookmarkId, Long walkwayId) {
        markedWalkwayJpaRepository.deleteByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }

    @Override
    public void deleteById(Long bookmarkId) {
        bookmarkJpaRepository.deleteById(bookmarkId);
        markedWalkwayJpaRepository.deleteAllByBookmarkId(bookmarkId);
    }

    @Override
    public Long save(Long memberId, String name) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        BookmarkEntity bookmarkEntity = new BookmarkEntity(name, memberEntity);
        return bookmarkJpaRepository.save(bookmarkEntity).getId();
    }

    @Override
    public boolean existsById(Long bookmarkId) {
        return bookmarkJpaRepository.existsById(bookmarkId);
    }


    public List<BookmarkEntity> getBookmarks(BookmarkEntity lastBookmarkEntity, Long memberId, Integer size) {
        return queryFactory.selectFrom(bookmark)
                .where(bookmark.member.id.eq(memberId),
                        lastBookmarkEntity == null
                                ? null
                                : bookmarkCreatedAtLt(lastBookmarkEntity.getCreatedAt()))
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
    }

    private BooleanExpression bookmarkCreatedAtLt(LocalDateTime createdAt){
        return createdAt != null ? bookmark.createdAt.lt(createdAt) : null;
    }

    public List<BookmarksWithMarkedWalkwayDTO> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId, BookmarkEntity lastBookmarkEntity, Integer size) {
        return queryFactory.select(Projections.constructor(
                        BookmarksWithMarkedWalkwayDTO.class,
                        bookmark.id,
                        bookmark.member.id,
                        bookmark.name,
                        markedWalkway.id
                ))
                .from(bookmark)
                .leftJoin(markedWalkway)
                .on(markedWalkway.walkway.id.eq(walkwayId).and(markedWalkway.bookmark.id.eq(bookmark.id)))
                .where(bookmark.member.id.eq(memberId),
                        lastBookmarkEntity == null
                                ? null
                                : bookmarkCreatedAtLt(lastBookmarkEntity.getCreatedAt())
                )
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
    }



    public List<MarkedWalkway> getBookmarkWalkway(Long bookmarkId, Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return queryFactory.select(markedWalkway)
                .from(markedWalkway)
                .join(markedWalkway.walkway).fetchJoin()
                .where(markedWalkway.bookmark.id.eq(bookmarkId), markedBookmarkCreatedAtLt(lastCreatedAt),
                        markedWalkway.walkway.member.id.eq(memberId)
                                .or(markedWalkway.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .orderBy(markedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * 북마크에 추가된 시간이 walkwayId가 북마크에 추가된 시간보다 작아야 한다. (markedBookmark의 createdAt이 더 작은 markedWalkay를 조회)
     * @param lastCreatedAt 마지막으로 가져온 markedBookmark의 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression markedBookmarkCreatedAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null;
    }


}
