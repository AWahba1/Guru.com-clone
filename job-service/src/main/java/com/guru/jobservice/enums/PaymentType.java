package com.guru.jobservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


//@Getter
@AllArgsConstructor
public enum PaymentType {
    FIXED("fixed"),
    HOURLY("hourly"),
    NOT_SURE("not sure");

   // @JsonValue
    private final String value;

    public String getValue() {
        return value;
    }
}


