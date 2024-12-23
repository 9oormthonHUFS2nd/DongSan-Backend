package com.dongsan.domains.dev.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.dev.dto.request.GenerateTokenRequest;
import com.dongsan.domains.dev.dto.response.GenerateTokenResponse;
import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
import com.dongsan.domains.dev.usecase.DevUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
@Tag(name = "👩🏻‍💻 개발용 API", description = "Develop API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevTokenController {
    private final DevUseCase devUseCase;

    @Operation(summary = "개발용 토큰 발급")
    @PostMapping("/token")
    public ResponseEntity<SuccessResponse<GenerateTokenResponse>> generateToken(
            @RequestBody GenerateTokenRequest request
    ){
        GenerateTokenResponse response = devUseCase.generateToken(request.memberId());
        return ResponseFactory.ok(response);
    }

    @Operation(summary = "accessToken으로 member 정보 확인하기")
    @GetMapping("/members")
    public ResponseEntity<SuccessResponse<GetMemberInfoResponse>> getMemberInfo(
            @RequestParam @NotBlank String accessToken
    ){
        GetMemberInfoResponse response = devUseCase.getMemberInfo(accessToken);
        return ResponseFactory.ok(response);
    }

}
