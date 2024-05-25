package com.guru.freelancerservice.commands.portfolio_commands;


import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class GetServiceCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private UUID serviceId;
    private final FreelancerService freelancerService;

    public GetServiceCommand(FreelancerService freelancerService, UUID serviceId) {
        this.freelancerService = freelancerService;
        this.serviceId = serviceId;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.getService(serviceId);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
