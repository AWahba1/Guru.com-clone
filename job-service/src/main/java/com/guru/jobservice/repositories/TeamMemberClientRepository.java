package com.guru.jobservice.repositories;

import com.guru.jobservice.dtos.TeamMemberClientView;
import com.guru.jobservice.model.TeamMemberClient;
import com.guru.jobservice.model.TeamMemberClient.TeamMemberClientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamMemberClientRepository extends JpaRepository<TeamMemberClient, TeamMemberClientId> {

    @Procedure(procedureName = "add_team_member_client")
    void addTeamMemberClient(UUID _owner_id, UUID _team_member_id, String _role, String _email);

    @Procedure(procedureName = "remove_team_member_client")
    void removeTeamMemberClient(UUID _owner_id, UUID _team_member_id);

    @Procedure(procedureName = "edit_team_member_role_client")
    void editTeamMemberRoleClient(UUID _owner_id, UUID _team_member_id, String _new_role);

    @Query(value = "SELECT * FROM view_team_members_client(?1)", nativeQuery = true)
    List<TeamMemberClientView> viewTeamMembersClient(UUID ownerId);
}
