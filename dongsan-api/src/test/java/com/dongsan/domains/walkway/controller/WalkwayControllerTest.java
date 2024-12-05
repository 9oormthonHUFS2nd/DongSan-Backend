package com.dongsan.domains.walkway.controller;

import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.entity.Bookmark;
import com.dongsan.domains.bookmark.mapper.BookmarksWithMarkedWalkwayMapper;
import com.dongsan.domains.bookmark.usecase.BookmarkUseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwaySearchResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.domains.walkway.usecase.WalkwayUseCase;
import com.dongsan.error.code.SystemErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fixture.BookmarkFixture;
import fixture.WalkwayFixture;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = WalkwayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayController Unit Test")
class WalkwayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WalkwayUseCase walkwayUseCase;

    @MockBean
    BookmarkUseCase bookmarkUseCase;

    @MockBean
    WalkwayQueryService walkwayQueryService;

    final Member member = createMemberWithId(1L);
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

    @Nested
    @DisplayName("createWalkway 메서드는")
    class Describe_createWalkway {

        @Test
        @DisplayName("request body를 전달 받으면 생성한 walkwayId를 반환한다.")
        void it_returns_walkwayId() throws Exception {
            // Given
            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
                    "testName",
                    "testMemo",
                    4.2,
                    20,
                    List.of("하나", "둘"),
                    "공개",
                    List.of(List.of(127.001, 37.001), List.of(127.002, 37.002))
            );

            when(walkwayUseCase.createWalkway(createWalkwayRequest, customOAuth2User.getMemberId())).thenReturn(
                    new CreateWalkwayResponse(1L));

            // When
            ResultActions response = mockMvc.perform(post("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));

            // Then
            response.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.walkwayId").value(1L));
        }

        @Test
        @DisplayName("request body의 name이나 course가 유효하지 않으면 INVALID_ARGUMENT_ERROR를 반환한다.")
        void it_returns_INVALID_ARGUMENT_ERROR() throws Exception {
            // Given
            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
                    "",
                    "testMemo",
                    4.2,
                    20,
                    List.of("하나", "둘"),
                    "공개",
                    List.of(List.of())
            );

            // When
            ResultActions response = mockMvc.perform(post("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));

            // Then
            response.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(SystemErrorCode.INVALID_ARGUMENT_ERROR.getCode()));
        }
    }


    @Nested
    @DisplayName("getWalkway 메서드는")
    class Describe_getWalkway {

        @Test
        @DisplayName("walkwayId를 전달 받으면 DTO 반환한다.")
        void it_returns_DTO() throws Exception {
            // Given
            Long walkwayId = 1L;

            GetWalkwayWithLikedResponse getWalkwayWithLikedResponse = GetWalkwayWithLikedResponse.builder()
                    .name("test")
                    .build();

            when(walkwayUseCase.getWalkwayWithLiked(walkwayId, customOAuth2User.getMemberId())).thenReturn(
                    getWalkwayWithLikedResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getWalkwayWithLikedResponse)));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("test"));
        }
    }

    @Nested
    @DisplayName("getWalkwaysSearch 메서드는")
    class Describe_getWalkwaysSearch {

        GetWalkwaySearchResponse getWalkwaySearchResponse;

        @BeforeEach
        void setUp() {
            List<Walkway> walkways = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                walkways.add(WalkwayFixture.createWalkway(member));
            }

            getWalkwaySearchResponse = WalkwayMapper.toGetWalkwaySearchResponse(walkways, 10);
        }

        @Test
        @DisplayName("type이 liked이면 좋아요 순으로 DTO를 반환한다.")
        void it_returns_DTO_liked() throws Exception {
            // given
            String type = "liked";
            Double latitude = 1.0;
            Double longitude = 1.0;
            Double distance = 1.3;
            String hashtags = "test";
            Long lastId = 1L;
            Double rating = 0.0;
            Integer likes = 1000;
            int size = 10;

            when(walkwayUseCase.getWalkwaysSearch(customOAuth2User.getMemberId(), type, latitude, longitude, distance,
                    hashtags, lastId, rating,
                    likes, size))
                    .thenReturn(getWalkwaySearchResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("type", "liked")
                    .param("hashtags", "test")
                    .param("latitude", "1.0")
                    .param("longitude", "1.0")
                    .param("distance", "1.3")
                    .param("lastId", "1")
                    .param("likes", "1000")
                    .param("size", "10")
                    .content(objectMapper.writeValueAsString(getWalkwaySearchResponse)));
            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.walkways").isArray())
                    .andExpect(jsonPath("$.data.walkways").isNotEmpty())
                    .andExpect(jsonPath("$.data.walkways.size()").value(10));
        }

        @Test
        @DisplayName("type이 rating이면 좋아요 순으로 DTO를 반환한다.")
        void it_returns_DTO_rating() throws Exception {
            // given
            String type = "rating";
            Double latitude = 1.0;
            Double longitude = 1.0;
            Double distance = 1.3;
            String hashtags = "test";
            Long lastId = 1L;
            Double rating = 5.0;
            Integer likes = 0;
            int size = 10;

            when(walkwayUseCase.getWalkwaysSearch(customOAuth2User.getMemberId(), type, latitude, longitude, distance,
                    hashtags, lastId, rating,
                    likes, size))
                    .thenReturn(getWalkwaySearchResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("type", "rating")
                    .param("hashtags", "test")
                    .param("latitude", "1.0")
                    .param("longitude", "1.0")
                    .param("distance", "1.3")
                    .param("lastId", "1")
                    .param("rating", "5.0")
                    .param("size", "10")
                    .content(objectMapper.writeValueAsString(getWalkwaySearchResponse)));
            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.walkways").isArray())
                    .andExpect(jsonPath("$.data.walkways").isNotEmpty())
                    .andExpect(jsonPath("$.data.walkways.size()").value(10));
        }
    }

    @Nested
    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
    class Describe_getBookmarksWithMarkedWalkway {
        @Test
        @DisplayName("marked 여부를 포함한 bookmark 리스트를 반환한다.")
        void it_returns_bookmarks() throws Exception {
            // Given
            Long walkwayId = 1L;
            List<Bookmark> bookmarks = new ArrayList<>();

            for(int i = 0; i < 5; i++) {
                Bookmark bookmark = BookmarkFixture.createBookmark(null);
                bookmarks.add(bookmark);
            }

            BookmarksWithMarkedWalkwayResponse bookmarksWithMarkedWalkwayResponse =
                    BookmarksWithMarkedWalkwayMapper.toBookmarksWithMarkedWalkwayResponse(bookmarks);

            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
            when(bookmarkUseCase.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId))
                    .thenReturn(bookmarksWithMarkedWalkwayResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/" + walkwayId + "/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.bookmarks").isArray())
                    .andExpect(jsonPath("$.data.bookmarks.size()").value(5));
        }
    }
}