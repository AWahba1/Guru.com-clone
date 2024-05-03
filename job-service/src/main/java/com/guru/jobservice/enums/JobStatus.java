package com.guru.jobservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum JobStatus {
    UNDER_REVIEW("Under Review"),
    OPEN("Open"),
    CLOSED("Closed"),
    NOT_APPROVED("Not Approved");

    private final String value;
}
