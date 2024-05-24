package com.guru.jobservice.dtos;

import java.util.UUID;

public class TeamMemberRequest {
    private UUID memberId;
    private String role;
    private String email;

    // Getters and Setters
    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
