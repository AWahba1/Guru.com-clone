package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.dtos.resources.ResourceDto;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

public class UpdateResourceCommand {
    private final FreelancerService freelancerService;
    private final ResourceDto resourceDto;
    private ResponseEntity<?> responseEntity;

    public UpdateResourceCommand(FreelancerService freelancerService, ResourceDto resourceDto) {
        this.freelancerService = freelancerService;
        this.resourceDto = resourceDto;
    }

    public void execute() {
        responseEntity = freelancerService.updateDedicatedResource(resourceDto);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}
