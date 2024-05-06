package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.models.FeaturedTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.UUID;

public interface FeaturedTeamMemberRepository extends JpaRepository<FeaturedTeamMember, UUID> {
    /*
    IN _freelancer_id uuid,
    IN _member_names varchar(255) ARRAY,
    IN _titles varchar(255) ARRAY,
    IN _member_types varchar(255) ARRAY,
    IN _member_emails varchar(255) ARRAY
     */
    @Procedure(name = "add_featured_team_members")
    void add_featured_team_members(UUID freelancer_id, String[] member_names, String[] titles, String[] member_types, String[] member_emails);
}
