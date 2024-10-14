package com.dongsan.domains.walkway.dto.request;

import com.dongsan.common.validation.annotation.NotEmptyWalkwayCourse;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateWalkwayRequest(
        @NotBlank(message = "산책로 제목을 입력해주세요.")
        String name,
        String memo,
        Double distance,
        Integer time,
        List<String> hashTags,
        String exposeLevel,
//      나중에 추가
//        MultipartFile courseImage,

        @NotEmptyWalkwayCourse
        List<List<Double>> course
) {
}
