package com.dongsan.domains.walkway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개")
    ;

    private final String description;
}
