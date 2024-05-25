package com.guru.freelancerservice.commands.team_commands;


import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class GetTeamMembersCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private UUID freelancerId;
    private final FreelancerService freelancerService;

    public GetTeamMembersCommand(FreelancerService freelancerService, UUID freelancerId) {
        this.freelancerService = freelancerService;
        this.freelancerId = freelancerId;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.getFreelancerTeamMembers(freelancerId);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
