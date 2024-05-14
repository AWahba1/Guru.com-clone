package com.guru.jobservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum HoursPerWeek {
    ONE_TO_TEN("1-10"),
    TEN_TO_THIRTY("10-30"),
    THIRTY_PLUS("30+");

    private final String value;
}
