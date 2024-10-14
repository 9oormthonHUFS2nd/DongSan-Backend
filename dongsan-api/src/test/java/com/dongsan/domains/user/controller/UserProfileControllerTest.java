package com.dongsan.domains.user.controller;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.dto.response.GetBookmarksResponse;
import com.dongsan.domains.user.dto.response.GetProfileResponse;
import com.dongsan.domains.user.usecase.UserProfileUseCase;
import org.hamcrest.CoreMatchers;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static fixture.MemberFixture.createMember;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileController Unit Test")
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileUseCase userProfileUsecase;


    @Nested
    @DisplayName("getUserProfile 메서드는")
    class Describe_getUserProfile {

        @Test
        @DisplayName("유저가 존재하면 프로필을 조회한다.")
        void it_returns_userProfile() throws Exception {

            // Given
            GetProfileResponse getProfileResponse = GetProfileResponse.builder()
                            .profileImageUrl("Test Url")
                            .email("test@gmail.com")
                            .nickname("Test Nickname")
                            .build();

            when(userProfileUsecase.getUserProfile(1L)).thenReturn(getProfileResponse);

            // When
            ResultActions response = mockMvc.perform(get("/users/profile"));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.profileImageUrl").value(getProfileResponse.profileImageUrl()))
                    .andExpect(jsonPath("$.data.email").value(getProfileResponse.email()))
                    .andExpect(jsonPath("$.data.nickname").value(getProfileResponse.nickname()));

        }

    }

    @Nested
    @DisplayName("getUserBookmarks 메서드는")
    class Describe_getUserBookmarks {
        @Test
        @DisplayName("북마크가 존재하면 북마크를 아이디 기준 내림차순으로 반환한다.")
        void getUserBookmarks() throws Exception {
            // Given
            List<GetBookmarksResponse.BookmarkInfo> bookmarkInfoList = new ArrayList<>();

            Member member = createMember();

            for(long id = 2L; id != 0L; id--) {
                GetBookmarksResponse.BookmarkInfo bookmarkInfo =
                        GetBookmarksResponse.BookmarkInfo.builder()
                                .bookmarkId(id)
                                .title("test" + id)
                                .build();
                bookmarkInfoList.add(bookmarkInfo);
            }

            GetBookmarksResponse getBookmarksResponse = new GetBookmarksResponse(bookmarkInfoList);

            Integer limit = 2;
            Long userId = 1L;
            Long bookmarkId = 3L;

            when(userProfileUsecase.getUserBookmarks(userId, bookmarkId, limit)).thenReturn(getBookmarksResponse);

            // When
            ResultActions response = mockMvc.perform(get("/users/bookmarks/title")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("bookmarkId", "3")
                    .param("limit", "2"));


            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.bookmarks.size()", CoreMatchers.is(limit)))
                    .andExpect(jsonPath("$.data.bookmarks[0].title").value(getBookmarksResponse.bookmarks().get(0).title()))
                    .andExpect(jsonPath("$.data.bookmarks[1].title").value(getBookmarksResponse.bookmarks().get(1).title()));

        }
    }
}