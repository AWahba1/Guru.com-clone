package com.guru.freelancerservice.services;

import com.guru.freelancerservice.dtos.FreelancerAboutSectionDto;
import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.PortfolioDto;
import com.guru.freelancerservice.models.Freelancer;

import java.util.List;
import java.util.UUID;

public interface FreelancerService {
    FreelancerProfileDto getFreelancerProfile(UUID freelancer_id);

    List<Freelancer> getAllFreelancers();

    Freelancer getFreelancer(UUID freeLancerId);

    boolean updateFreelancerAbout(FreelancerAboutSectionDto freelancerAboutSectionDto);

    boolean updateFreelancerAboutVisibility(UUID freelancer_id);

    boolean addPortfolio(PortfolioDto portfolioDto);

    boolean unpublishPortfolio(UUID portfolio_id);

    boolean deletePortfolio(UUID portfolio_id);
}
