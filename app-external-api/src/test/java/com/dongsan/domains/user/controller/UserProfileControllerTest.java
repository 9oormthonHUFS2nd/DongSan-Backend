package com.dongsan.domains.user.controller;

import static fixture.MemberFixture.createMemberWithId;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.bookmark.controller.GetBookmarksResponse;
import com.dongsan.domains.bookmark.controller.GetBookmarksResponse.BookmarkInfo;
import com.dongsan.core.domains.member.MemberService;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
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

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileController Unit Test")
class UserProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService userProfileUsecase;

    final Member member = createMemberWithId(1L);
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

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

            when(userProfileUsecase.getUserProfile(member.getId())).thenReturn(getProfileResponse);

            // When
            ResultActions response = mockMvc.perform(get("/users/profile"));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.profileImageUrl").value(getProfileResponse.profileImageUrl()))
                    .andExpect(jsonPath("$.data.email").value(getProfileResponse.email()))
                    .andExpect(jsonPath("$.data.nickname").value(getProfileResponse.nickname()))
                    .andReturn();
        }

    }

    @Nested
    @DisplayName("getUserBookmarks 메서드는")
    class Describe_getUserBookmarks {
        @Test
        @DisplayName("북마크가 존재하면 북마크를 아이디 기준 내림차순으로 반환한다.")
        void getUserBookmarks() throws Exception {
            // Given
            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();

            for(long id = 2L; id != 0L; id--) {
                BookmarkInfo bookmarkInfo = BookmarkInfo.builder()
                                .bookmarkId(id)
                                .title("test" + id)
                                .build();
                bookmarkInfoList.add(bookmarkInfo);
            }

            GetBookmarksResponse getBookmarksResponse = new GetBookmarksResponse(bookmarkInfoList, true);

            Integer limit = 2;
            Long userId = member.getId();
            Long bookmarkId = 3L;

            when(userProfileUsecase.getUserBookmarks(userId, bookmarkId, limit)).thenReturn(getBookmarksResponse);

            // When
            ResultActions response = mockMvc.perform(get("/users/bookmarks/title")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("lastId", "3")
                    .param("size", "2"));


            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.bookmarks.size()", CoreMatchers.is(limit)))
                    .andExpect(jsonPath("$.data.bookmarks[0].title").value(getBookmarksResponse.bookmarks().get(0).title()))
                    .andExpect(jsonPath("$.data.bookmarks[1].title").value(getBookmarksResponse.bookmarks().get(1).title()));
        }
    }
}