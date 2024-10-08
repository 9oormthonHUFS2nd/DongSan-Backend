package com.dongsan.domains.bookmark.service;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.repository.BookmarkRepository;
import com.dongsan.domains.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    @DisplayName("사용자 북마크 리스트(페이징)")
    void readUserBookmarks() {

        // Given
        Bookmark bookmark1 = Bookmark.builder()
                .name("test1")
                .build();

        Bookmark bookmark2 = Bookmark.builder()
                .name("test2")
                .build();

        List<Bookmark> mockBookmarks = Arrays.asList(bookmark2, bookmark1);

        Long bookmarkId = 3L;
        Integer size = 2;
        Member member = Member.builder().build();
        Pageable pageable = PageRequest.of(0, size);

        when(bookmarkRepository.findBookmarksByIdAndMember(bookmarkId, member, pageable)).thenReturn(mockBookmarks);

        // When
        List<Bookmark> bookmarkListReturn = bookmarkService.readUserBookmarks(bookmarkId, member, size);

        // Then
        Assertions.assertThat(bookmarkListReturn).isNotNull();
        Assertions.assertThat(bookmarkListReturn.size()).isEqualTo(size);
        Assertions.assertThat(bookmarkListReturn.get(0).getName()).isEqualTo(bookmark2.getName());
        Assertions.assertThat(bookmarkListReturn.get(1).getName()).isEqualTo(bookmark1.getName());
    }
}