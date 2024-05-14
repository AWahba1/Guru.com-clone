package com.guru.jobservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SortOrder
{
    NEWEST("newest"),
    OLDEST("oldest"),
    CLOSING_SOON("closing_soon");

    private final String value;
}

