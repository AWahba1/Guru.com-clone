package com.guru.freelancerservice.commands.dedicatedResource_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public class GetAllFreelancerResources {
    private ResponseEntity<?> responseEntity;
    private final FreelancerService freelancerService;
    private final UUID freelancerId;

    public GetAllFreelancerResources(FreelancerService freelancerService, UUID freelancerId) {
        this.freelancerService = freelancerService;
        this.freelancerId = freelancerId;
    }

    public void execute() {
        responseEntity = freelancerService.getAllFreelancerDedicatedResources(freelancerId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}
