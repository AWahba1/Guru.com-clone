package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.*;
import com.guru.freelancerservice.models.*;
import com.guru.freelancerservice.repositories.DedicatedResoruceRepository;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.repositories.PortfolioRepository;
import com.guru.freelancerservice.repositories.ServiceRepository;
import com.guru.freelancerservice.response.ResponseHandler;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final PortfolioRepository portfolioRepository;
    private final ServiceRepository serviceRepository;
    private final DedicatedResoruceRepository dedicatedResourceRepository;
    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository, PortfolioRepository portfolioRepository, ServiceRepository serviceRepository, DedicatedResoruceRepository dedicatedResourceRepository) {
        this.freelancerRepository = freelancerRepository;
        this.portfolioRepository = portfolioRepository;
        this.serviceRepository = serviceRepository;
        this.dedicatedResourceRepository = dedicatedResourceRepository;
    }

    @Override
    public FreelancerProfileDto getFreelancerProfile(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return null;
        }
//        List<Object[]> profile = freelancerRepository.getFreelancerProfile(freelancer_id);
//        FreelancerProfileDto freelancerProfileDto = new FreelancerProfileDto();
//        for (Object[] object : profile) {
//            freelancerProfileDto.setFreelancer(getFreelancerFromObject(object));
//            freelancerProfileDto.setResourceSkills(populateSkills((Object[]) object[22]));
//            freelancerProfileDto.setServiceSkills(populateSkills((Object[]) object[23]));
//        }

        return freelancerRepository.getFreelancerProfile(freelancer_id).getFirst();
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        return freelancerRepository.findAll();
    }

    @Override
    public Freelancer getFreelancer(UUID freeLancerId) {
        return freelancerRepository.findById(freeLancerId).orElse(null);
    }


    @Override
    public boolean updateFreelancerAbout(FreelancerAboutSectionDto freelancerAboutSectionDto) {
        Freelancer freelancer = freelancerRepository.findById(freelancerAboutSectionDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return false;
        }

        freelancerRepository.update_freelancer_profile_about_section(
                freelancerAboutSectionDto.getFreelancer_id(),
                freelancerAboutSectionDto.getFreelancer_name(),
                freelancerAboutSectionDto.getImage_url(),
                freelancerAboutSectionDto.getTagline(),
                freelancerAboutSectionDto.getBio(),
                freelancerAboutSectionDto.getWork_terms(),
                freelancerAboutSectionDto.getAttachments(),
                freelancerAboutSectionDto.getUser_type().toString(),
                freelancerAboutSectionDto.getWebsite_link(),
                freelancerAboutSectionDto.getFacebook_link(),
                freelancerAboutSectionDto.getLinkedin_link(),
                freelancerAboutSectionDto.getProfessional_video_link(),
                freelancerAboutSectionDto.getCompany_history(),
                freelancerAboutSectionDto.getOperating_since()
        );
        return true;

    }

    @Override
    public boolean updateFreelancerAboutVisibility(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return false;
        }
        freelancerRepository.toggle_profile_visibility(freelancer_id);
        return true;
    }

    @Override
    public boolean addPortfolio(PortfolioDto portfolioDto) {
        Freelancer freelancer = freelancerRepository.findById(portfolioDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return false;
        }
        portfolioRepository.add_portfolio(
                portfolioDto.getFreelancer_id(),
                portfolioDto.getTitle(),
                portfolioDto.getCover_image_url(),
                portfolioDto.getAttachments()
        );
        return true;
    }

    @Override
    public boolean unpublishPortfolio(UUID portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.unpublish_portfolio(portfolio_id);
        return true;
    }

    @Override
    public boolean deletePortfolio(UUID portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.delete_portfolio(portfolio_id);
        return true;
    }

    @Override
    public boolean updatePortfolio(PortfolioDto portfolioDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioDto.getPortfolio_id()).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.update_portfolio(
                portfolioDto.getPortfolio_id(),
                portfolioDto.getTitle(),
                portfolioDto.getCover_image_url(),
                portfolioDto.getAttachments()
        );
        return true;
    }

    @Override
    public ResponseEntity<Object> addService(ServiceDto serviceDto) {
        Freelancer freelancer = freelancerRepository.findById(serviceDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        List<UUID> freelancerPortfolioIds = portfolioRepository.getPortfolioIds(serviceDto.getFreelancer_id());
        if (!new HashSet<>(freelancerPortfolioIds).containsAll(List.of(serviceDto.getPortfolio_id()))) {
            return  ResponseHandler.generateErrorResponse("Some or all of these portfolios don't belong to this freelancer", HttpStatus.BAD_REQUEST);
        }
        serviceRepository.add_service(
                serviceDto.getFreelancer_id(),
                serviceDto.getService_title(),
                serviceDto.getService_description(),
                serviceDto.getService_skills(),
                serviceDto.getService_rate(),
                serviceDto.getMinimum_budget(),
                serviceDto.getService_thumbnail(),
                serviceDto.getPortfolio_id()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer service added successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> unpublishService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        serviceRepository.unpublish_service(service_id);
        return ResponseHandler.generateGeneralResponse("Freelancer service unpublished successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        serviceRepository.delete_service(service_id);
        return ResponseHandler.generateGeneralResponse("Freelancer service deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateService(ServiceDto serviceDto) {
        ServiceModel service = serviceRepository.findById(serviceDto.getService_id()).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        List<UUID> freelancerPortfolioIds = portfolioRepository.getPortfolioIds(serviceDto.getFreelancer_id());
        if (!new HashSet<>(freelancerPortfolioIds).containsAll(List.of(serviceDto.getPortfolio_id()))) {
            return  ResponseHandler.generateErrorResponse("Some or all of these portfolios don't belong to this freelancer", HttpStatus.BAD_REQUEST);
        }
        serviceRepository.update_service(
                serviceDto.getService_id(),
                serviceDto.getService_title(),
                serviceDto.getService_description(),
                serviceDto.getService_skills(),
                serviceDto.getService_rate(),
                serviceDto.getMinimum_budget(),
                serviceDto.getService_thumbnail(),
                serviceDto.getPortfolio_id()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer service updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addDedicatedResource(ResourceDto resourceDto) {
        Freelancer freelancer = freelancerRepository.findById(resourceDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.add_dedicated_resource(
                resourceDto.getFreelancer_id(),
                resourceDto.getResource_name(),
                resourceDto.getResource_title(),
                resourceDto.getResource_summary(),
                resourceDto.getResource_skills(),
                resourceDto.getResource_rate(),
                resourceDto.getMinimum_duration(),
                resourceDto.getResource_image(),
                resourceDto.getPortfolio_ids()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource added successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> unpublishDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.unpublish_dedicated_resource(resource_id);
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource unpublished successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.delete_dedicated_resource(resource_id);
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateDedicatedResource(ResourceDto resourceDto) {
        return null;
    }
}
