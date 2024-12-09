package com.dongsan.domains.bookmark.service;

import static fixture.BookmarkFixture.createBookmark;
import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
import static fixture.ReflectFixture.reflectField;
import static fixture.WalkwayFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.entity.MarkedWalkway;
import com.dongsan.domains.bookmark.repository.MarkedWalkwayRepository;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.error.code.BookmarkErrorCode;
import com.dongsan.error.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarkedWalkwayQueryService Unit Test")
class MarkedWalkwayQueryServiceTest {
    @InjectMocks
    MarkedWalkwayQueryService markedWalkwayQueryService;
    @Mock
    MarkedWalkwayRepository markedWalkwayRepository;

    @Nested
    @DisplayName("getCreatedAt 메서드는")
    class Describe_getCreatedAt{
        @Test
        @DisplayName("walkway가 null이면 null을 반환한다.")
        void it_returns_null(){
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = null;

            // when
            LocalDateTime result = markedWalkwayQueryService.getCreatedAt(bookmark, walkway);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("북마크에 산책로가 존재하지 않으면 예외를 반환한다.")
        void it_throws_exception(){
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", 2L);
            when(markedWalkwayRepository.findByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
                    Optional.empty());

            // when & then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                markedWalkwayQueryService.getCreatedAt(bookmark, walkway);
            });
            assertEquals(BookmarkErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }

        @Test
        @DisplayName("북마크에 산책로가 존재하면 북마크가 산책로에 추가된 시간을 반환한다.")
        void it_returns_createdAt(){
            // given
            Bookmark bookmark = createBookmark(null);
            reflectField(bookmark, "id", 1L);
            Walkway walkway = createWalkway(null);
            reflectField(walkway, "id", 2L);
            MarkedWalkway markedWalkway = createMarkedWalkway(walkway, bookmark);
            reflectField(markedWalkway, "createdAt", LocalDateTime.of(2024, 12, 9, 11, 11));
            when(markedWalkwayRepository.findByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
                    Optional.of(markedWalkway));

            // when
            LocalDateTime result = markedWalkwayQueryService.getCreatedAt(bookmark, walkway);

            // then
            assertThat(result).isEqualTo(markedWalkway.getCreatedAt());
        }
    }
}