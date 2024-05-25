package com.guru.freelancerservice.commands.team_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
public class DeleteTeamMemberCommand {
    private final FreelancerService freelancerService;
    private final UUID teamMemberId;
    private ResponseEntity<?> responseEntity;

    public DeleteTeamMemberCommand(FreelancerService freelancerService, UUID teamMemberId) {
        this.freelancerService = freelancerService;
        this.teamMemberId = teamMemberId;
    }

    public void execute() {
        responseEntity = freelancerService.deleteTeamMember(teamMemberId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
