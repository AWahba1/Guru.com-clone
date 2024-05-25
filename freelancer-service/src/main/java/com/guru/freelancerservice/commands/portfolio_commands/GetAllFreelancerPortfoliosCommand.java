package com.guru.freelancerservice.commands.portfolio_commands;

import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class GetAllFreelancerPortfoliosCommand {
    private ResponseEntity<?> responseEntity;
    private final FreelancerService freelancerService;
    private final UUID freelancerId;

    public GetAllFreelancerPortfoliosCommand(FreelancerService freelancerService, UUID freelancerId) {
        this.freelancerService = freelancerService;
        this.freelancerId = freelancerId;
    }

    public void execute() {
        responseEntity = freelancerService.getAllFreelancerPortfolios(freelancerId);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
