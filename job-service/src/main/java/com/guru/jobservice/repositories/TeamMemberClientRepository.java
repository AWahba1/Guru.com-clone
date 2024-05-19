package com.guru.jobservice.repositories;

import com.guru.jobservice.dtos.TeamMemberClientView;
import com.guru.jobservice.model.TeamMemberClient;
import com.guru.jobservice.model.TeamMemberClient.TeamMemberClientId;
import com.guru.jobservice.model.TeamMemberClient.TeamMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamMemberClientRepository extends JpaRepository<TeamMemberClient, TeamMemberClientId> {

    @Procedure("add_team_member_client")
    ResponseEntity<Object> addTeamMemberClient(UUID _owner_id, UUID _team_member_id, TeamMemberRole _role, String _email);

    @Procedure("remove_team_member_client")
    ResponseEntity<Object> removeTeamMemberClient(UUID _owner_id, UUID _team_member_id);

    @Procedure("edit_team_member_role_client")
    ResponseEntity<Object> editTeamMemberRoleClient(UUID _owner_id, UUID _team_member_id, TeamMemberRole _new_role);

    @Query(value = "SELECT * FROM view_team_members_client(?1)", nativeQuery = true)
    List<TeamMemberClientView> viewTeamMembersClient(UUID ownerId);
}