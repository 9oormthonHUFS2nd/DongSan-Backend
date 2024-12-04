package com.dongsan.domains.bookmark.controller;

import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.service.BookmarkQueryService;
import com.dongsan.domains.bookmark.usecase.BookmarkUseCase;
import com.dongsan.domains.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkController Unit Test")
class BookmarkControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookmarkUseCase bookmarkUseCase;
    @MockBean
    BookmarkQueryService bookmarkQueryService;;
    final Member member = createMemberWithId(1L);
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

    @Nested
    @DisplayName("createBookmark 메서드는")
    class Describe_createBookmark{
        @Test
        @DisplayName("DTO에 name이 공백이면 예외를 반환한다.")
        void it_returns_400_when_name_is_blank() throws Exception{
            // given
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name(" ")
                    .build();
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                        .content(requestBody)
                        .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("DTO에 name이 null이면 예외를 반환한다.")
        void it_returns_400_when_name_is_null() throws Exception{
            // given
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name(null)
                    .build();
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("생성한 북마크의 Id를 response DTO로 반환한다.")
        void it_returns_responseDTO() throws Exception{
            // given
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name("북마크1")
                    .build();
            BookmarkIdResponse response = BookmarkIdResponse.builder()
                    .bookmarkId(1L)
                    .build();
            when(bookmarkUseCase.createBookmark(customOAuth2User.getMemberId(), request)).thenReturn(response);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.bookmarkId").value(response.bookmarkId()))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("renameBookmark 메서드는")
    class Describe_renameBookmark{
        @Test
        @DisplayName("DTO에 name이 공백이면 예외를 반환한다.")
        void it_returns_400_when_name_is_blank() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name(" ")
                    .build();
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("bookmarkId가 존재하지 않으면 예외를 반환한다.")
        void it_returns_400_when_bookmarkId_not_exist() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name("북마크1")
                    .build();
            when(bookmarkQueryService.existsById(bookmarkId)).thenReturn(false);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("북마크 이름 변경을 완료하면 201을 반환한다.")
        void it_returns_201() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = BookmarkNameRequest.builder()
                    .name("북마크1")
                    .build();
            when(bookmarkQueryService.existsById(bookmarkId)).thenReturn(true);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isNoContent())
                    .andReturn();
        }
    }
}
