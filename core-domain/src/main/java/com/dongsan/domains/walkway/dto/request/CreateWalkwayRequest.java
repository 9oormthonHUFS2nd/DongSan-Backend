package com.dongsan.domains.walkway.dto.request;

import com.dongsan.domains.walkway.enums.ExposeLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateWalkwayRequest(
        @NotNull
        Long courseImageId,
        @NotBlank(message = "산책로 제목을 입력해주세요.")
        String name,
        String memo,
        Double distance,
        Integer time,
        @NotEmpty(message = "해쉬태그를 입력해주세요.")
        List<String> hashtags,
        ExposeLevel exposeLevel,
        List<List<Double>> course
) {
}
