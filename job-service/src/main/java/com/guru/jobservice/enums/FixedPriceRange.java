package com.guru.jobservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FixedPriceRange {
    UNDER_250("Under $250"),
    BETWEEN_250_AND_500("$250 to $500"),
    BETWEEN_500_AND_1000("$500 to $1,000"),
    BETWEEN_1000_AND_2500("$1,000 to $2,500"),
    BETWEEN_2500_AND_5000("$2,500 to $5,000");

    private final String value;
}
