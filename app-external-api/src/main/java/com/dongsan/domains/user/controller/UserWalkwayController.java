package com.dongsan.domains.user.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.user.response.WalkwayListResponse;
import com.dongsan.domains.user.usecase.UserWalkwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/walkways")
@Tag(name = "사용자가 등록한 산책로", description = "User Walkway")
@RequiredArgsConstructor
@Slf4j
public class UserWalkwayController {
    private final UserWalkwayUseCase userWalkwayUseCase;

    /**
     * 내가 등록한 산책로 조회
     * @param size 한번에 몇개 조회할 건지
     * @param lastId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "등록한 산책로 조회")
    @GetMapping("/upload")
    public ResponseEntity<SuccessResponse<WalkwayListResponse>> getUserUploadWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        WalkwayListResponse response = userWalkwayUseCase.getUserUploadWalkway(customOAuth2User.getMemberId(), size, lastId);
        return ResponseFactory.ok(response);
    }

    /**
     * 내가 좋아요한 산책로 상세 보기
     * @param size 한번에 몇개 조회할 건지
     * @param lastId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "좋아요한 산책로 조회")
    @GetMapping("/like")
    public ResponseEntity<SuccessResponse<WalkwayListResponse>> getUserLikedWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        WalkwayListResponse response = userWalkwayUseCase.getUserLikedWalkway(customOAuth2User.getMemberId(), size, lastId);
        return ResponseFactory.ok(response);
    }

}
