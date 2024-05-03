package com.guru.jobservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobDuration {
    LESS_THAN_1_WEEK("Less than 1 week"),
    LESS_THAN_1_MONTH("Less than 1 month"),
    ONE_TO_THREE_MONTHS("1 to 3 months"),
    THREE_TO_SIX_MONTHS("3 to 6 months"),
    ONGOING("More than 6 months / ongoing");

    private final String value;

}
