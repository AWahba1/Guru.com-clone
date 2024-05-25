package com.guru.freelancerservice.commands.service_commands;

import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class PublishServiceCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private UUID serviceId;
    private final FreelancerService freelancerService;

    public PublishServiceCommand(FreelancerService freelancerService, UUID serviceId) {
        this.freelancerService = freelancerService;
        this.serviceId = serviceId;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.publishService(serviceId);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
