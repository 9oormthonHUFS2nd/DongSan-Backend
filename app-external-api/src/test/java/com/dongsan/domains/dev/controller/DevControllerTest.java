package com.dongsan.domains.dev.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.core.domains.auth.AuthService;
import com.dongsan.api.domains.dev.DevController;
import com.dongsan.api.domains.dev.GenerateTokenRequest;
import com.dongsan.api.domains.dev.GenerateTokenResponse;
import com.dongsan.api.domains.dev.GetMemberInfoResponse;
import com.dongsan.api.domains.dev.DevUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(DevController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("DevTokenController Unit Test")
class DevControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    DevUseCase devUseCase;
    @MockBean
    AuthService authService;

    @Nested
    @DisplayName("generateToken 메소드는")
    class Describe_generateToken{
        @Test
        @DisplayName("사용자가 존재하면 accessToken과 refreshToken을 발급한다.")
        void it_returns_tokens() throws Exception{
            // given
            GenerateTokenRequest request = GenerateTokenRequest.builder()
                    .memberId(1L)
                    .build();
            String requestBody = objectMapper.writeValueAsString(request);
            GenerateTokenResponse response = GenerateTokenResponse.builder()
                    .accessToken("ac_t")
                    .refreshToken("rf_t")
                    .build();
            when(devUseCase.generateToken(request.memberId())).thenReturn(response);

            // when & then
            mockMvc.perform(post("/dev/token")
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                    .andExpect(jsonPath("$.data.refreshToken").value(response.refreshToken()))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("getMemberInfo 메소드는")
    class Describe_getMemberInfoEntity {
        @Test
        @DisplayName("사용자가 존재하면 사용자 정보를 반환한다.")
        void it_returns_memberInfo() throws Exception{
            // given
            String accessToken = "ac_t";
            GetMemberInfoResponse response = GetMemberInfoResponse.builder()
                    .memberId(1L)
                    .email("abc@naver.com")
                    .build();
            when(devUseCase.getMemberInfo(accessToken)).thenReturn(response);

            // when & then
            mockMvc.perform(get("/dev/members")
                            .param("accessToken", accessToken)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.memberId").value(response.memberId()))
                    .andExpect(jsonPath("$.data.email").value(response.email()))
                    .andReturn();
        }

    }
}