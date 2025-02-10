package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.api.domains.walkway.mapper.LikedWalkwayMapper;
import com.dongsan.core.common.apiResponse.ResponseFactory;
import com.dongsan.core.common.apiResponse.SuccessResponse;
import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.usecase.LikedWalkwayService;
import com.dongsan.core.domains.walkway.usecase.WalkwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/walkways")
@Tag(name = "산책로")
@RequiredArgsConstructor
@Validated
public class LikedWalkwayController {
    private final WalkwayService walkwayService;

    @Operation(summary = "산책로 좋아요")
    @PostMapping("/{walkwayId}/likes")
    public ResponseEntity<SuccessResponse<Object>> createLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        LikedWalkway likedWalkway = LikedWalkwayMapper.toLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        walkwayService.createLikedWalkway(likedWalkway);
        return ResponseFactory.created(null);
    }

    @Operation(summary = "산책로 좋아요 취소")
    @DeleteMapping("/{walkwayId}/likes")
    public ResponseEntity<SuccessResponse<Object>> deleteLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        LikedWalkway likedWalkway = LikedWalkwayMapper.toLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        walkwayService.deleteLikedWalkway(likedWalkway);
        return ResponseFactory.ok(null);
    }
}
