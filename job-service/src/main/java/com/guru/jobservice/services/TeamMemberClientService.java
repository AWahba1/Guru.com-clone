package com.guru.jobservice.services;

import com.guru.jobservice.dtos.TeamMemberClientView;
import com.guru.jobservice.dtos.TeamMemberRequest;
import com.guru.jobservice.repositories.TeamMemberClientRepository;
import com.guru.jobservice.response.ResponseHandler;
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

    public ResponseEntity<Object> addTeamMemberClient(UUID ownerId, TeamMemberRequest teamMemberRequest) {
        try {
            if (!teamMemberRequest.getRole().equals("ADMINISTRATOR") &&
                    !teamMemberRequest.getRole().equals("MANAGER") &&
                    !teamMemberRequest.getRole().equals("COORDINATOR")) {
                return ResponseHandler.generateErrorResponse("Role must be ADMINISTRATOR or MANAGER or COORDINATOR", HttpStatus.NOT_ACCEPTABLE);
            }
        teamMemberClientRepository.addTeamMemberClient(
                ownerId,
                teamMemberRequest.getMemberId(),
                teamMemberRequest.getRole(),
                teamMemberRequest.getEmail()
        );
            return ResponseHandler.generateGeneralResponse("Team member added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error adding team member: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> removeTeamMemberClient(UUID ownerId, TeamMemberRequest teamMemberRequest) {
        try {
            teamMemberClientRepository.removeTeamMemberClient(ownerId, teamMemberRequest.getMemberId());
            return ResponseHandler.generateGeneralResponse("Team member removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error removing team member: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> editTeamMemberRoleClient(UUID ownerId, TeamMemberRequest teamMemberRequest) {
        try {
            if (!teamMemberRequest.getRole().equals("ADMINISTRATOR") &&
                    !teamMemberRequest.getRole().equals("MANAGER") &&
                    !teamMemberRequest.getRole().equals("COORDINATOR")) {
                return ResponseHandler.generateErrorResponse("Role must be ADMINISTRATOR or MANAGER or COORDINATOR", HttpStatus.NOT_ACCEPTABLE);
            }
            teamMemberClientRepository.editTeamMemberRoleClient(ownerId, teamMemberRequest.getMemberId(), teamMemberRequest.getRole());
            return ResponseHandler.generateGeneralResponse("Team member role updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error updating team member role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> viewTeamMembersClient(UUID ownerId) {
        try {
            List<TeamMemberClientView> teamMembers = teamMemberClientRepository.viewTeamMembersClient(ownerId);
            return ResponseHandler.generateGetResponse("Team members retrieved successfully", HttpStatus.OK, teamMembers, teamMembers.size());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Error retrieving team members: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}