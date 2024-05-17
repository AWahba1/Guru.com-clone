package com.gurutest.Users;

public enum UserStatus {
    VERIFIED("Verified"),
    PENDING_VERIFICATION("Pending Verification"),
    INACTIVE("Inactive");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

