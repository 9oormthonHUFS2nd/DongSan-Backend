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
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dev")
@Tag(name = "👩🏻‍💻 개발용 API", description = "Develop API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevController {
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

    @Operation(summary = "이미지 s3 저장 확인하기")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> uploadImage(
            @RequestPart("file") MultipartFile image
    )throws IOException {
        String url = devUseCase.uploadImage(image);
        return ResponseFactory.ok(url);
    }

}
