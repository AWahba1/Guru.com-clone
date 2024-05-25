package com.guru.freelancerservice.commands.portfolio_commands;


import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class GetResourceCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private UUID resourceID;
    private final FreelancerService freelancerService;

    public GetResourceCommand(FreelancerService freelancerService, UUID resourceID) {
        this.freelancerService = freelancerService;
        this.resourceID = resourceID;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.getDedicatedResource(resourceID);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
