package com.dongsan.domains.user.response;

import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record WalkwayListResponse(Set<WalkwayResponse> walkways) {
    public WalkwayListResponse(List<Walkway> walkways) {
        this(walkways.stream()
                .map(WalkwayResponse::new)
                .collect(Collectors.toSet())
        );
    }
}
