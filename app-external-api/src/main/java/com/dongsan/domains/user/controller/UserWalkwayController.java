package com.dongsan.domains.user.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.user.response.WalkwayListResponse;
import com.dongsan.domains.user.usecase.UserWalkwayUseCase;
import com.dongsan.domains.walkway.dto.response.GetWalkwayHistoriesResponse;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.usecase.WalkwayHistoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/walkways")
@Tag(name = "마이페이지")
@RequiredArgsConstructor
public class UserWalkwayController {
    private final UserWalkwayUseCase userWalkwayUseCase;
    private final WalkwayHistoryUseCase walkwayHistoryUseCase;

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

    @Operation(summary = "회원의 리뷰 작성 가능한 산책로 이용 기록 모두 보기")
    @GetMapping("/history")
    public ResponseEntity<SuccessResponse<GetWalkwayHistoriesResponse>> getUserWalkwayHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        List<WalkwayHistory> walkwayHistories = walkwayHistoryUseCase.getUserWalkwayHistories(customOAuth2User.getMemberId(), lastId, size);
        return ResponseFactory.ok(GetWalkwayHistoriesResponse.from(walkwayHistories));
    }

}
