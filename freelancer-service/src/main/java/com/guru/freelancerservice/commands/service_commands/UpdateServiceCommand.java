package com.guru.freelancerservice.commands.service_commands;

import com.guru.freelancerservice.commands.Command;
import com.guru.freelancerservice.dtos.services.ServiceDto;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;

public class UpdateServiceCommand implements Command {
    private ResponseEntity<?> responseEntity;
    private ServiceDto serviceDto;
    private final FreelancerService freelancerService;

    public UpdateServiceCommand(FreelancerService freelancerService, ServiceDto serviceDto) {
        this.freelancerService = freelancerService;
        this.serviceDto = serviceDto;
    }

    @Override
    public void execute() {
        responseEntity = freelancerService.updateService(serviceDto);

    }
    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }


}
