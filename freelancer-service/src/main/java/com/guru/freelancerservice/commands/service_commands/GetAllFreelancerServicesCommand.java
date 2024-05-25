package com.guru.freelancerservice.commands.service_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class GetAllFreelancerServicesCommand {
    private ResponseEntity<?> responseEntity;
    private final FreelancerService freelancerService;
    private final UUID freelancerId;

    public GetAllFreelancerServicesCommand(FreelancerService freelancerService, UUID freelancerId) {
        this.freelancerService = freelancerService;
        this.freelancerId = freelancerId;
    }

    public void execute() {
        responseEntity = freelancerService.getAllFreelancerServices(freelancerId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}
