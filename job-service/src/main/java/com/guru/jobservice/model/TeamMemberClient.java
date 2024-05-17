package com.guru.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(TeamMemberClient.TeamMemberClientId.class)
@Table(name = "team_members_client")
public class TeamMemberClient {

    @Id
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @Id
    @ManyToOne
    @JoinColumn(name = "team_member_id", referencedColumnName = "id", nullable = false)
    private User teamMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private TeamMemberRole role;

    @Column(name = "email", nullable = false)
    private String email;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamMemberClientId implements Serializable {
        private UUID owner;
        private UUID teamMember;
    }

    public enum TeamMemberRole {
        ADMINISTRATOR,
        MANAGER,
        COORDINATOR
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "users")
    public static class User {
        @Id
        private UUID id;

        @Column(name = "is_verified")
        private Boolean isVerified;

        @Column(name = "amount_spent", precision = 10, scale = 2)
        private Double amountSpent;

        @Column(name = "name", length = 255)
        private String name;
    }
}