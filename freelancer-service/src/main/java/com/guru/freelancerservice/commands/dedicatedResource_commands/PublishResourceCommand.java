package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class PublishResourceCommand {
    private final FreelancerService freelancerService;
    private UUID resourceId;
    private ResponseEntity<?> responseEntity;

    public PublishResourceCommand(FreelancerService freelancerService, UUID resourceId) {
        this.freelancerService = freelancerService;
        this.resourceId = resourceId;
    }

    public void execute() {
        responseEntity = freelancerService.publishDedicatedResource(resourceId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
