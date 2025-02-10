//package com.dongsan.domains.bookmark.repository;
//
//import static fixture.BookmarkFixture.createBookmark;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
//import com.dongsan.rdb.domains.bookmark.BookmarkJpaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("BookmarkRepository Unit Test")
//class BookmarkEntityRepositoryTest extends RepositoryTest {
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private BookmarkJpaRepository bookmarkJpaRepository;
//
//    @Nested
//    @DisplayName("deleteById 메서드는")
//    class Describe_deleteById{
//        @BeforeEach
//        void setUp(){
//            for(int i=0; i<5; i++){
//                BookmarkEntity bookmarkEntity = createBookmark(null);
//                em.persist(bookmarkEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("bookmarkId와 일치하는 Bookmark를 삭제한다.")
//        void it_deletes_bookmark(){
//            // given
//            Long bookmarkId = 1L;
//
//            // when
//            bookmarkJpaRepository.deleteById(bookmarkId);
//
//            // then
//            boolean exist = bookmarkJpaRepository.existsById(bookmarkId);
//            assertThat(exist).isFalse();
//        }
//    }
//}