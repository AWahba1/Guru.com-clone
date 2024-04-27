package com.guru.freelancerservice.services;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;

import java.util.UUID;

public interface FreelancerService {
    FreelancerProfileDto getFreelancerProfile(UUID freelancer_id);

}
