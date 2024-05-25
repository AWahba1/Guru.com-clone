package com.guru.freelancerservice.commands.portfolio_commands;


import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class PublishPortfolioCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private UUID portfolioId;
    private final FreelancerService freelancerService;

    public PublishPortfolioCommand(FreelancerService freelancerService, UUID portfolioId) {
        this.freelancerService = freelancerService;
        this.portfolioId = portfolioId;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.publishPortfolio(portfolioId);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
