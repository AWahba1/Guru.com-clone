package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final FreelancerService freelancerService ;

    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository, FreelancerService freelancerService) {
        this.freelancerRepository = freelancerRepository;
        this.freelancerService = freelancerService;
    }

    @Override
    public FreelancerProfileDto getFreelancerProfile(UUID freelancer_id) {
        FreelancerProfileDto profile = freelancerService.getFreelancerProfile(freelancer_id);

        return profile;
    }
}
