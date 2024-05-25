package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.dtos.resources.ResourceDto;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;


public class AddResourceCommand implements Command {
    private final FreelancerService freelancerService;
    private final ResourceDto resourceDto;
    private ResponseEntity<?> responseEntity;

    public AddResourceCommand(FreelancerService freelancerService, ResourceDto resourceDto) {
        this.freelancerService = freelancerService;
        this.resourceDto = resourceDto;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.addDedicatedResource(resourceDto);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
