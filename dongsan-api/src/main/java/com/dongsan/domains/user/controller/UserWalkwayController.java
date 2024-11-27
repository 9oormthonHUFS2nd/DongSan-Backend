package com.dongsan.domains.user.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.common.validation.annotation.ExistWalkway;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.user.dto.response.GetWalkwayDetailResponse;
import com.dongsan.domains.user.dto.response.GetWalkwaySummaryResponse;
import com.dongsan.domains.user.usecase.UserWalkwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/walkways")
@Tag(name = "사용자가 등록한 산책로", description = "User Walkway")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserWalkwayController {
    private final UserWalkwayUseCase userWalkwayUseCase;

    /**
     * 내가 등록한 산책로를 마이페이지에서 간단하게 보기
     * @param size 한번에 몇개 조회할 건지
     * @param walkwayId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "내가 등록한 산책로 요약 보기")
    @GetMapping("/summary")
    public ResponseEntity<SuccessResponse<GetWalkwaySummaryResponse>> getUserWalkwaySummary(
            @RequestParam(defaultValue = "5") Integer size,
            @ExistWalkway @RequestParam(required = false) Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        log.info("[userid] ", customOAuth2User.getMemberId());
        GetWalkwaySummaryResponse response = userWalkwayUseCase.getUserWalkwaySummary(customOAuth2User.getMemberId(), size, walkwayId);
        return ResponseFactory.ok(response);
    }

    /**
     * 내가 등록한 산책로를 상세 보기
     * @param size 한번에 몇개 조회할 건지
     * @param walkwayId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     * @return
     */
    @Operation(summary = "내가 등록한 산책로 상세 보기")
    @GetMapping()
    public ResponseEntity<SuccessResponse<GetWalkwayDetailResponse>> getUserWalkwayDetail(
            @RequestParam(defaultValue = "10") Integer size,
            @ExistWalkway @RequestParam(required = false) Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        GetWalkwayDetailResponse response = userWalkwayUseCase.getUserWalkwayDetail(customOAuth2User.getMemberId(), size, walkwayId);
        return ResponseFactory.ok(response);
    }


}
