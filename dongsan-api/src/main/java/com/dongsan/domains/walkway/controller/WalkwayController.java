package com.dongsan.domains.walkway.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwaySearchResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.usecase.WalkwayUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkways")
@RequiredArgsConstructor
public class WalkwayController {

    private final WalkwayUseCase walkwayUseCase;

    @PostMapping("")
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest
    ) {
        return ResponseFactory.created(walkwayUseCase.createWalkway(createWalkwayRequest, 1L));
    }

    @GetMapping("/{walkwayId}")
    public ResponseEntity<SuccessResponse<GetWalkwayWithLikedResponse>> getWalkway(
            @PathVariable Long walkwayId
    ) {
        return ResponseFactory.ok(walkwayUseCase.getWalkwayWithLiked(walkwayId, 1L));
    }

    @GetMapping("")
    public ResponseEntity<SuccessResponse<GetWalkwaySearchResponse>> getWalkwaysSearch(
            @RequestParam String type,
            @RequestParam(defaultValue = "") String hashtags,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double distance,
            @RequestParam(defaultValue = "0") Long lastId,
            @RequestParam(defaultValue = "0") Double rating,
            @RequestParam(defaultValue = "0") Integer likes,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseFactory.ok(walkwayUseCase.getWalkwaysSearch(1L, type, latitude, longitude, distance, hashtags, lastId, rating, likes, size));
    }
}
