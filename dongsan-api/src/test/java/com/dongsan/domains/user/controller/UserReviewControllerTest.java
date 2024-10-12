package com.dongsan.domains.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.domains.review.service.ReviewQueryService;
import com.dongsan.domains.user.dto.response.GetReview;
import com.dongsan.domains.user.usecase.UserReviewUseCase;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserReviewController Unit Test")
class UserReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserReviewUseCase userReviewUseCase;
    @MockBean
    private ReviewQueryService reviewQueryService;

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{

        @Test
        @DisplayName("리뷰가 존재하면 작성한 리뷰를 생성 날짜를 기준으로 내림차순으로 조회한다.")
        void it_returns_reviews() throws Exception{
            // given
            int limit = 5;
            long reviewId = 10L;
            long memberId = 1L;
            GetReview.ReviewInfo reviewInfo = new GetReview.ReviewInfo(
                    1L, 10L, "Sample Walkway", "2024-10-10", 5, "This is a great walkway!");
            GetReview response = GetReview.builder()
                    .reviews(Collections.singletonList(reviewInfo))
                    .build();

            // when
            when(reviewQueryService.existsByReviewId(reviewId)).thenReturn(true);
            when(userReviewUseCase.getReviews(limit, reviewId, memberId)).thenReturn(response);

            // then
            mockMvc.perform(get("/users/reviews")
                            .param("limit", String.valueOf(limit))
                            .param("reviewId", String.valueOf(reviewId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.reviews", hasSize(1)))
                    .andDo(print())
                    .andReturn();
        }

        @Test
        @DisplayName("전달한 마지막 리뷰ID 존재하지 않으면 400를 반환한다.")
        void it_returns_400() throws Exception{
            // given
            int limit = 5;
            long reviewId = 10L;

            // when
            when(reviewQueryService.existsByReviewId(reviewId)).thenReturn(false);

            // then
            mockMvc.perform(get("/users/reviews")
                            .param("limit", String.valueOf(limit))
                            .param("reviewId", String.valueOf(reviewId))
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn();
        }
    }



}