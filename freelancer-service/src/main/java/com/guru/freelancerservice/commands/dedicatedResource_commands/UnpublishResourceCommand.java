package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class UnpublishResourceCommand {
    private final FreelancerService freelancerService;
private final UUID resourceId;
private ResponseEntity<?> responseEntity;

public UnpublishResourceCommand(FreelancerService freelancerService, UUID resourceId) {
    this.freelancerService = freelancerService;
    this.resourceId = resourceId;
}

public void execute() {
    responseEntity = freelancerService.unpublishDedicatedResource(resourceId);
}

public ResponseEntity<?> getResponse() {
    return responseEntity;}

}
