package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public class DeleteResourceCommand {
    private final FreelancerService freelancerService;
    private final UUID resourceId;
    private ResponseEntity<?> responseEntity;

    public DeleteResourceCommand(FreelancerService freelancerService, UUID resourceId) {
        this.freelancerService = freelancerService;
        this.resourceId = resourceId;
    }

    public void execute() {
        responseEntity = freelancerService.deleteDedicatedResource(resourceId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
