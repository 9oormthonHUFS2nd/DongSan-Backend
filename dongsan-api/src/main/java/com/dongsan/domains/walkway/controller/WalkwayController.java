package com.dongsan.domains.walkway.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.usecase.WalkwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkways")
@Tag(name = "🚶🏻‍산책로", description = "Walkway")
@RequiredArgsConstructor
public class WalkwayController {

    private final WalkwayUseCase walkwayUseCase;

    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest
    ) {
        return ResponseFactory.created(walkwayUseCase.createWalkway(createWalkwayRequest, 1L));
    }
}
