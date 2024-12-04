package com.dongsan.domains.bookmark.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.dto.request.BookmarkNameRequest;
import com.dongsan.domains.bookmark.dto.response.BookmarkIdResponse;
import com.dongsan.domains.bookmark.usecase.BookmarkUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "북마크 API", description = "Bookmark API")
@Validated
public class BookmarkController {
    private final BookmarkUseCase bookmarkUseCase;

    @PostMapping()
    public ResponseEntity<SuccessResponse<BookmarkIdResponse>> createBookmark(
            @Valid @RequestBody BookmarkNameRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        BookmarkIdResponse response = bookmarkUseCase.createBookmark(customOAuth2User.getMemberId(), request);
        return ResponseFactory.created(response);
    }
}
