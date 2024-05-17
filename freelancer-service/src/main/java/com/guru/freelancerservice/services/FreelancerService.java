package com.guru.freelancerservice.services;

import com.guru.freelancerservice.dtos.featuredTeammember.AddFeaturedTeamMembersRequestDto;
import com.guru.freelancerservice.dtos.freelancer.FreelancerAboutSectionDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioRequestDto;
import com.guru.freelancerservice.dtos.resources.ResourceDto;
import com.guru.freelancerservice.dtos.services.ServiceDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Quote;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface FreelancerService {
    ResponseEntity<Object> getFreelancerProfile(UUID freelancer_id, UUID viewer_id);

    List<Freelancer> getAllFreelancers() throws InterruptedException;

    Freelancer getFreelancer(UUID freeLancerId);

    boolean updateFreelancerAbout(FreelancerAboutSectionDto freelancerAboutSectionDto);

    boolean updateFreelancerAboutVisibility(UUID freelancer_id);

    boolean addPortfolio(PortfolioRequestDto portfolioRequestDto);

    boolean unpublishPortfolio(UUID portfolio_id);

    ResponseEntity<Object> publishPortfolio(UUID portfolio_id);

    boolean deletePortfolio(UUID portfolio_id);

    boolean updatePortfolio(PortfolioRequestDto portfolioRequestDto);

    ResponseEntity<Object> addService(ServiceDto serviceDto);

    ResponseEntity<Object> unpublishService(UUID service_id);

    ResponseEntity<Object> publishService(UUID service_id);

    ResponseEntity<Object> deleteService(UUID service_id);

    ResponseEntity<Object> updateService(ServiceDto serviceDto);

    ResponseEntity<Object> addDedicatedResource(ResourceDto resourceDto);

    ResponseEntity<Object> unpublishDedicatedResource(UUID resource_id);

    ResponseEntity<Object> publishDedicatedResource(UUID resource_id);

    ResponseEntity<Object> deleteDedicatedResource(UUID resource_id);

    ResponseEntity<Object> updateDedicatedResource(ResourceDto resourceDto);

//    ResponseEntity<Object> addQuote(Quote quote);

    ResponseEntity<Object> getPortfolio(UUID portfolio_id);

    ResponseEntity<Object> getAllFreelancerPortfolios(UUID freelancer_id);

    ResponseEntity<Object> getService(UUID service_id);

    ResponseEntity<Object> getAllFreelancerServices(UUID freelancer_id);

    ResponseEntity<Object> getDedicatedResource(UUID resource_id);

    ResponseEntity<Object> getAllFreelancerDedicatedResources(UUID freelancer_id);

    ResponseEntity<Object> addFeaturedTeamMembers(AddFeaturedTeamMembersRequestDto addFeaturedTeamMembersRequestDto);

    ResponseEntity<Object> deleteTeamMember(UUID team_member_id);

    ResponseEntity<Object> getFreelancerTeamMembers(UUID freelancer_id);

    ResponseEntity<Object> addToFavourites(UUID clientId, UUID freelancerId);

    ResponseEntity<Object> removeFromFavourites(UUID clientId, UUID freelancerId);

    ResponseEntity<Object> getClientFavourites(UUID clientId);

}
