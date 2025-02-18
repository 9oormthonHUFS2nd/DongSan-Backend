package com.dongsan.api.domains.walkway.dto.request;

<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/domains/walkway/dto/request/CreateWalkwayRequest.java
import com.dongsan.api.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
=======
import com.dongsan.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.domains.walkway.enums.ExposeLevel;
>>>>>>> 920be9371ff304630f249d16536e70a3e734d4d6:core-domain/src/main/java/com/dongsan/domains/walkway/dto/request/CreateWalkwayRequest.java
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateWalkwayRequest(
        @NotNull
        Long courseImageId,
        @NotBlank(message = "산책로 제목을 입력해주세요.")
        String name,
        String memo,
        @DecimalMin("0.2")
        Double distance,
        @Min(600)
        Integer time,
        @NotNull
        List<String> hashtags,
        ExposeLevel exposeLevel,
        List<WalkwayCoordinate> course
) {
}
