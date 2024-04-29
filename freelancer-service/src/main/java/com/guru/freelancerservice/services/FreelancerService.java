package com.guru.freelancerservice.services;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;

import java.util.List;
import java.util.UUID;

public interface FreelancerService {
    FreelancerProfileDto getFreelancerProfile(UUID freelancer_id);

    List<Freelancer> getAllFreelancers();

    Freelancer getFreelancer(UUID freeLancerId);
}
