package com.guru.jobservice.services;

import com.guru.jobservice.model.TeamMemberClient;
import com.guru.jobservice.model.TeamMemberClient.TeamMemberRole;
import com.guru.jobservice.repositories.TeamMemberClientRepository;
import com.guru.jobservice.response.ResponseHandler;
import com.guru.jobservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamMemberClientService {

    private final TeamMemberClientRepository teamMemberClientRepository;
    private final EmailService emailService;

    @Autowired
    public TeamMemberClientService(TeamMemberClientRepository teamMemberClientRepository, EmailService emailService) {
        this.teamMemberClientRepository = teamMemberClientRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<Object> addTeamMemberClient(UUID ownerId, UUID teamMemberId, TeamMemberRole role, String email) {
        try {
            teamMemberClientRepository.addTeamMemberClient(ownerId, teamMemberId, role, email);
            emailService.sendEmail(email, "Team Invitation", "You have been invited to work with owner ID: " + ownerId);
            return ResponseHandler.generateGeneralResponse("Team member added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error adding team member: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> removeTeamMemberClient(UUID ownerId, UUID teamMemberId) {
        try {
            teamMemberClientRepository.removeTeamMemberClient(ownerId, teamMemberId);
            return ResponseHandler.generateGeneralResponse("Team member removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error removing team member: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> editTeamMemberRoleClient(UUID ownerId, UUID teamMemberId, TeamMemberRole newRole) {
        try {
            teamMemberClientRepository.editTeamMemberRoleClient(ownerId, teamMemberId, newRole);
            return ResponseHandler.generateGeneralResponse("Team member role updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error updating team member role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> viewTeamMembersClient(UUID ownerId) {
        try {
            List<TeamMemberClient> teamMembers = teamMemberClientRepository.viewTeamMembersClient(ownerId);
            return ResponseHandler.generateGetResponse("Team members retrieved successfully", HttpStatus.OK, teamMembers, teamMembers.size());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error retrieving team members: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
