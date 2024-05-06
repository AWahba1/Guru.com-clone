package com.guru.freelancerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "freelancers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeaturedTeamMember {
//    team_member_id UUID PRIMARY KEY,
//    freelancer_id UUID,
//    member_name VARCHAR(255),
//    title VARCHAR(255) CHECK (title IN ('CONSULTANT', 'MANAGER')),
//    member_type VARCHAR(255) CHECK (member_type IN ('INDEPENDENT_ACCOUNTS', 'SUB_ACCOUNTS', 'NO_ACCESS_MEMBERS'))
//    member_email VARCHAR(255),

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID team_member_id;
    private UUID freelancer_id;
    private String member_name;
    private String title;
    private String member_type;
    private String member_email;
}
