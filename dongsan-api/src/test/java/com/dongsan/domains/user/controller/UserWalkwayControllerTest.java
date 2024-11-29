package com.dongsan.domains.user.controller;

import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse.GetWalkwayDetailInfo;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse.UserWalkwaySummaryInfo;
import com.dongsan.domains.user.usecase.UserWalkwayUseCase;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
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

import java.time.LocalDate;
import java.util.List;

import static fixture.MemberFixture.createMemberWithId;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserWalkwayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserWalkwayController Unit Test")
class UserWalkwayControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserWalkwayUseCase userWalkwayUseCase;
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
    @DisplayName("getUserWalkwaySummary 메서드는")
    class Describe_getUserWalkwaySummary{
        @Test
        @DisplayName("walkwayId가 존재하지 않으면 404을 반환한다.")
        void it_returns_404() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(false);

            // when & then
            mockMvc.perform(get("/users/walkways/summary")
                    .param("size", String.valueOf(size))
                    .param("walkwayId", String.valueOf(walkwayId))
                    .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("산책로가 존재하면 산책로를 반환한다.")
        void it_returns_walkways() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            GetWalkwaySummaryResponse response = GetWalkwaySummaryResponse.builder()
                    .walkways(
                            List.of(UserWalkwaySummaryInfo.builder()
                            .walkwayId(1L)
                            .name("첫 번째 산책로")
                            .date(LocalDate.of(2024, 11, 28))
                            .distance(5.0)
                            .courseImageUrl("http://example.com/image1.jpg")
                            .build()))
                    .build();
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
            when(userWalkwayUseCase.getUserWalkwaySummary(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(response);

            // when & then
            mockMvc.perform(get("/users/walkways/summary")
                            .param("size", String.valueOf(size))
                            .param("walkwayId", String.valueOf(walkwayId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.walkways", hasSize(response.walkways().size())))
                    .andExpect(jsonPath("$.data.walkways[0].name", is(response.walkways().get(0).name())))
                    .andReturn();
        }

    }

    @Nested
    @DisplayName("getUserWalkwayDetail 메서드는")
    class Describe_getUserWalkwayDetail{
        @Test
        @DisplayName("walkwayId가 존재하지 않으면 400을 반환한다.")
        void it_returns_400() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(false);

            // when & then
            mockMvc.perform(get("/users/walkways")
                            .param("size", String.valueOf(size))
                            .param("walkwayId", String.valueOf(walkwayId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("산책로가 존재하면 산책로를 반환한다.")
        void it_returns_walkways() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            GetWalkwayDetailResponse response = GetWalkwayDetailResponse.builder()
                    .walkways(
                            List.of(GetWalkwayDetailInfo.builder()
                                    .walkwayId(1L)
                                    .name("첫 번째 산책로")
                                    .date(LocalDate.of(2024, 11, 28))
                                    .distance(5.0)
                                    .hashtags(List.of("공원", "산책"))
                                    .courseImageUrl("http://example.com/image1.jpg")
                                    .build())
                    )
                    .build();
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
            when(userWalkwayUseCase.getUserWalkwayDetail(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(response);

            // when & then
            mockMvc.perform(get("/users/walkways")
                            .param("size", String.valueOf(size))
                            .param("walkwayId", String.valueOf(walkwayId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.walkways", hasSize(response.walkways().size())))
                    .andExpect(jsonPath("$.data.walkways[0].name", is(response.walkways().get(0).name())))
                    .andExpect(jsonPath("$.data.walkways[0].hashtags", hasSize(response.walkways().get(0).hashtags().size())))
                    .andReturn();
        }

    }

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway{
        @Test
        @DisplayName("walkwayId가 존재하지 않으면 400을 반환한다.")
        void it_returns_400() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(false);

            // when & then
            mockMvc.perform(get("/users/walkways/like")
                            .param("size", String.valueOf(size))
                            .param("walkwayId", String.valueOf(walkwayId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        @DisplayName("walkwayId가 존재하면 산책로를 반환한다.")
        void it_returns_walkways() throws Exception{
            // given
            Integer size = 5;
            Long walkwayId = 1L;
            GetWalkwayDetailResponse response = GetWalkwayDetailResponse.builder()
                    .walkways(
                            List.of(GetWalkwayDetailInfo.builder()
                                    .walkwayId(1L)
                                    .name("첫 번째 산책로")
                                    .date(LocalDate.of(2024, 11, 28))
                                    .distance(5.0)
                                    .hashtags(List.of("공원", "산책"))
                                    .courseImageUrl("http://example.com/image1.jpg")
                                    .build())
                    )
                    .build();
            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
            when(userWalkwayUseCase.getUserLikedWalkway(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(response);

            // when & then
            mockMvc.perform(get("/users/walkways/like")
                            .param("size", String.valueOf(size))
                            .param("walkwayId", String.valueOf(walkwayId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.walkways", hasSize(response.walkways().size())))
                    .andExpect(jsonPath("$.data.walkways[0].name", is(response.walkways().get(0).name())))
                    .andExpect(jsonPath("$.data.walkways[0].hashtags", hasSize(response.walkways().get(0).hashtags().size())))
                    .andReturn();
        }

    }
}