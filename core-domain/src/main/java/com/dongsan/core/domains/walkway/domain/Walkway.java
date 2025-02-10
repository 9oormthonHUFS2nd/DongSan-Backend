package com.dongsan.core.domains.walkway.domain;

import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record Walkway(
        Long walkwayId,
        String name,
        LocalDateTime createdAt,
        String memo,
        Integer likeCount,
        Integer reviewCount,
        Double rating,
        List<String> hashtags,
        CourseInfo courseInfo,
        Author author,
        ExposeLevel exposeLevel
) {
    public void validateOwner(Long memberId) {
        if (!this.author.authorId().equals(memberId)) {
            throw new CoreException(CoreErrorCode.NOT_WALKWAY_OWNER);
        }
    }

}
