package com.dongsan.domains.user.controller;

import static fixture.MemberFixture.createMemberWithId;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.service.ReviewQueryService;
import com.dongsan.domains.user.response.GetReviewResponse;
import com.dongsan.domains.user.usecase.UserReviewUseCase;
import java.util.Collections;
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

@WebMvcTest(UserReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserReviewController Unit Test")
class UserReviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserReviewUseCase userReviewUseCase;
    @MockBean
    ReviewQueryService reviewQueryService;
    final Member member = createMemberWithId(1L);
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }


    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{

        @Test
        @DisplayName("리뷰를 생성 날짜를 기준으로 내림차순으로 조회한다.")
        void it_returns_reviews() throws Exception{
            // given
            int limit = 5;
            long reviewId = 10L;

            GetReviewResponse.ReviewInfo reviewInfo = new GetReviewResponse.ReviewInfo(
                    1L, 10L, "Sample Walkway", "2024-10-10", 5, "This is a great walkway!");
            GetReviewResponse response = GetReviewResponse.builder()
                    .reviews(Collections.singletonList(reviewInfo))
                    .build();
            when(reviewQueryService.existsByReviewId(reviewId)).thenReturn(true);
            when(userReviewUseCase.getReviews(limit, reviewId, customOAuth2User.getMemberId())).thenReturn(response);

            // when & then
            mockMvc.perform(get("/users/reviews")
                            .param("size", String.valueOf(limit))
                            .param("reviewId", String.valueOf(reviewId))
                            .contentType("application/json;charset=UTF-8")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.reviews", hasSize(1)))
                    .andDo(print())
                    .andReturn();
        }

//        reviewId 검증은 controller가 아닌, 비즈니스 레이어 에서 검증하는 걸로 변경
//        @Test
//        @DisplayName("전달한 마지막 리뷰ID 존재하지 않으면 400를 반환한다.")
//        void it_returns_400() throws Exception{
//            // given
//            int limit = 5;
//            long reviewId = 10L;
//            when(reviewQueryService.existsByReviewId(reviewId)).thenReturn(false);
//
//            // when & then
//            mockMvc.perform(get("/users/reviews")
//                            .param("size", String.valueOf(limit))
//                            .param("reviewId", String.valueOf(reviewId))
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn();
//        }
    }

}