package com.dongsan.domains.walkway.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.usecase.WalkwayUseCase;
import com.dongsan.error.code.SystemErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

@WebMvcTest(controllers = WalkwayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayController Unit Test")
class WalkwayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalkwayUseCase walkwayUseCase;

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

            when(walkwayUseCase.createWalkway(createWalkwayRequest, 1L)).thenReturn(new CreateWalkwayResponse(1L));

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
                    .andExpect(jsonPath("$.code").value(SystemErrorStatus.INVALID_ARGUMENT_ERROR.getCode()));
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

            when(walkwayUseCase.getWalkwayWithLiked(walkwayId, 1L)).thenReturn(getWalkwayWithLikedResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getWalkwayWithLikedResponse)));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("test"));
        }
    }
}