package com.dongsan.domains.user.controller;

import com.dongsan.domains.user.dto.UserBookmarkDto;
import com.dongsan.domains.user.dto.UserBookmarkDto.UserBookmarksRes.UserBookmarkRes;
import com.dongsan.domains.user.dto.UserProfileDto;
import com.dongsan.domains.user.usecase.UserProfileUsecase;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileUsecase userProfileUsecase;


    @Test
    @DisplayName("마이페이지 프로필 조회")
    void getUserProfile() throws Exception {

        // Given
        UserProfileDto.UserProfileRes userProfileRes =
                new UserProfileDto.UserProfileRes("Test Url", "test@gmail.com", "Test Nickname");

        when(userProfileUsecase.getUserProfile(1L)).thenReturn(userProfileRes);

        // When
        ResultActions response = mockMvc.perform(get("/users/profile"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.profileImageUrl").value(userProfileRes.profileImageUrl()))
                .andExpect(jsonPath("$.result.email").value(userProfileRes.email()))
                .andExpect(jsonPath("$.result.nickname").value(userProfileRes.nickname()));
    }

    @Test
    @DisplayName("마이페이지 북마크 조회")
    void getUserBookmarks() throws Exception {
        // Given
        List<UserBookmarkRes> bookmarkList = new ArrayList<>();

        for(long id = 2L; id != 0L; id--) {
            UserBookmarkRes bookmark = new UserBookmarkRes(id, "test"+id);
            bookmarkList.add(bookmark);
        }

        UserBookmarkDto.UserBookmarksRes userBookmarksRes =
                new UserBookmarkDto.UserBookmarksRes(bookmarkList);

        Integer size = 2;
        Long userId = 1L;
        Long bookmarkId = 3L;

        when(userProfileUsecase.getUserBookmarks(userId, bookmarkId, size)).thenReturn(userBookmarksRes);

        // When
        ResultActions response = mockMvc.perform(get("/users/bookmarks/title")
                .contentType(MediaType.APPLICATION_JSON)
                .param("bookmarkId", "3")
                .param("size", "2"));


        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.bookmarks.size()", CoreMatchers.is(size)))
                .andExpect(jsonPath("$.result.bookmarks[0].title").value(bookmarkList.get(0).title()))
                .andExpect(jsonPath("$.result.bookmarks[1].title").value(bookmarkList.get(1).title()));

    }
}