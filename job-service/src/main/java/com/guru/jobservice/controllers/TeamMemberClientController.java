package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.TeamMemberRequest;
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

    @PostMapping("/{id}")
    public ResponseEntity<Object> addTeamMember(
            @PathVariable UUID id, @RequestBody TeamMemberRequest teamMemberRequest) {
        return teamMemberClientService.addTeamMemberClient(id, teamMemberRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeTeamMember(
            @PathVariable UUID id, @RequestBody TeamMemberRequest teamMemberRequest) {
        return teamMemberClientService.removeTeamMemberClient(id, teamMemberRequest);
    }

    @PutMapping("/update-role/{id}")
    public ResponseEntity<Object> editTeamMemberRole(
            @PathVariable UUID id, @RequestBody TeamMemberRequest teamMemberRequest) {
        return teamMemberClientService.editTeamMemberRoleClient(id, teamMemberRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> viewTeamMembers(@PathVariable UUID id) {
        return teamMemberClientService.viewTeamMembersClient(id);
    }
}