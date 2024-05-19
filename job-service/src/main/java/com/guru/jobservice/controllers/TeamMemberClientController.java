package com.guru.jobservice.controllers;

import com.guru.jobservice.model.TeamMemberClient.TeamMemberRole;
import com.guru.jobservice.services.TeamMemberClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/team-members")
public class TeamMemberClientController {

    private final TeamMemberClientService teamMemberClientService;

    @Autowired
    public TeamMemberClientController(TeamMemberClientService teamMemberClientService) {
        this.teamMemberClientService = teamMemberClientService;
    }

    @PostMapping("/add/{ownerId}/{teamMemberId}/{role}/{email}")
    public ResponseEntity<Object> addTeamMember(
            @PathVariable UUID ownerId,
            @PathVariable UUID teamMemberId,
            @PathVariable TeamMemberRole role,
            @PathVariable String email) {
        return teamMemberClientService.addTeamMemberClient(ownerId, teamMemberId, role, email);
    }

    @DeleteMapping("/remove/{ownerId}/{teamMemberId}")
    public ResponseEntity<Object> removeTeamMember(
            @PathVariable UUID ownerId,
            @PathVariable UUID teamMemberId) {
        return teamMemberClientService.removeTeamMemberClient(ownerId, teamMemberId);
    }

    @PutMapping("/edit-role/{ownerId}/{teamMemberId}/{newRole}")
    public ResponseEntity<Object> editTeamMemberRole(
            @PathVariable UUID ownerId,
            @PathVariable UUID teamMemberId,
            @PathVariable TeamMemberRole newRole) {
        return teamMemberClientService.editTeamMemberRoleClient(ownerId, teamMemberId, newRole);
    }

    @GetMapping("/view/{ownerId}")
    public ResponseEntity<Object> viewTeamMembers(@PathVariable UUID ownerId) {
        return teamMemberClientService.viewTeamMembersClient(ownerId);
    }
}