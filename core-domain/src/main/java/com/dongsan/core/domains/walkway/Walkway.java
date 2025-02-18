package com.dongsan.core.domains.walkway;

import com.dongsan.core.support.util.Author;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.time.LocalDateTime;
import java.util.List;

public record Walkway(
        Long walkwayId,
        String name,
        LocalDateTime createdAt,
        String memo,
        Stat stat,
        List<String> hashtags,
        CourseInfo courseInfo,
        Author author,
        ExposeLevel exposeLevel
) {

}
