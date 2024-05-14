package com.guru.jobservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobVisibility {
    EVERYONE("Everyone"),
    GURU_FREELANCERS("Guru Freelancers"),
    INVITE_ONLY("Invite Only (on Guru)");

    private final String value;
}
