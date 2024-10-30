package com.dongsan.domains.walkway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개")
    ;

    private final String description;

    public static ExposeLevel getExposeLevelByDescription(String description) {
        return Arrays.stream(ExposeLevel.values())
                .filter(val -> val.description.equals(description))
                .findFirst()
                .orElse(ExposeLevel.PRIVATE);
    }

    public Boolean toBoolean() {
        return this.description.equals("공개");
    }
}
