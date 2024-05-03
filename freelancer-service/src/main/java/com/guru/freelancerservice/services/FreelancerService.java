package com.guru.freelancerservice.services;

import com.guru.freelancerservice.dtos.*;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Quote;
import org.springframework.http.ResponseEntity;

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

    boolean updatePortfolio(PortfolioDto portfolioDto);

    ResponseEntity<Object> addService(ServiceDto serviceDto);

    ResponseEntity<Object> unpublishService(UUID service_id);

    ResponseEntity<Object> deleteService(UUID service_id);

    ResponseEntity<Object> updateService(ServiceDto serviceDto);

    ResponseEntity<Object> addDedicatedResource(ResourceDto resourceDto);

    ResponseEntity<Object> unpublishDedicatedResource(UUID resource_id);

    ResponseEntity<Object> deleteDedicatedResource(UUID resource_id);

    ResponseEntity<Object> updateDedicatedResource(ResourceDto resourceDto);

    ResponseEntity<Object> addQuote(Quote quote);

    ResponseEntity<Object> getPortfolio(UUID portfolio_id);

    ResponseEntity<Object> getAllFreelancerPortfolios(UUID freelancer_id);

}
