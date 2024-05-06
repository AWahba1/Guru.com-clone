package com.guru.freelancerservice.controller;

import com.guru.freelancerservice.commands.EditFreelancerAboutSectionCommand;
import com.guru.freelancerservice.dtos.freelancer.FreelancerAboutSectionDto;
import com.guru.freelancerservice.dtos.freelancer.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioRequestDto;
import com.guru.freelancerservice.dtos.resources.ResourceDto;
import com.guru.freelancerservice.dtos.featuredTeammember.AddFeaturedTeamMembersRequestDto;
import com.guru.freelancerservice.dtos.services.ServiceDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Quote;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.guru.freelancerservice.response.ResponseHandler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {
    FreelancerService freelancerService;
    EditFreelancerAboutSectionCommand editFreelancerAboutSectionCommand;

    @Autowired
    public FreelancerController(FreelancerService freelancerService, EditFreelancerAboutSectionCommand editFreelancerAboutSectionCommand) {
        this.freelancerService = freelancerService;
        this.editFreelancerAboutSectionCommand = editFreelancerAboutSectionCommand;
    }

    @GetMapping
    public ResponseEntity<Object> getAllFreelancers(){
        List<Freelancer> freelancers = freelancerService.getAllFreelancers();
        return ResponseHandler.generateGetResponse("Freelancers retrieved successfully", HttpStatus.OK, freelancers, freelancers.size());
    }

    @GetMapping(path = "/{freelancer_id}")
    public ResponseEntity<Object> getFreelancer(@PathVariable("freelancer_id") UUID freeLancer_id) {
        Freelancer freelancer=freelancerService.getFreelancer(freeLancer_id);
        if(freelancer==null){
            return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateGetResponse("Freelancer fetched successfully", HttpStatus.OK, freelancer, 1);
    }

    @GetMapping(path = "/profile/{freelancer_id}")
    public ResponseEntity<Object> getFreelancerProfile(@PathVariable("freelancer_id") UUID freeLancer_id) {
        return freelancerService.getFreelancerProfile(freeLancer_id);
    }

    @PatchMapping(path = "/profile/about/{freelancer_id}")
    public ResponseEntity<Object> updateFreelancerAbout(@PathVariable("freelancer_id") UUID freelancer_id, @RequestBody FreelancerAboutSectionDto freelancerAboutSectionDto) {
        freelancerAboutSectionDto.setFreelancer_id(freelancer_id);
        boolean updated =freelancerService.updateFreelancerAbout(freelancerAboutSectionDto);

        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer about section updated successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/profile/about/toggle_visibility/{freelancer_id}")
    public ResponseEntity<Object> updateFreelancerAboutVisibility(@PathVariable("freelancer_id") UUID freelancer_id) {
        boolean updated =freelancerService.updateFreelancerAboutVisibility(freelancer_id);

        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer about section visibility updated successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
    }


    @PostMapping(path = "/portfolio/{freelancer_id}")
    public ResponseEntity<Object> addPortfolio(@PathVariable("freelancer_id") UUID freelancer_id,@RequestBody PortfolioRequestDto portfolioRequestDto) {
        portfolioRequestDto.setFreelancer_id(freelancer_id);
        boolean updated =freelancerService.addPortfolio(portfolioRequestDto);
        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer portfolio added successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/portfolio/unpublish/{portfolio_id}")
    public ResponseEntity<Object> unpublishPortfolio(@PathVariable("portfolio_id") UUID portfolio_id) {
        boolean updated =freelancerService.unpublishPortfolio(portfolio_id);
        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer portfolio unpublished successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Portfolio not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/portfolio/publish/{portfolio_id}")
    public ResponseEntity<Object> publishPortfolio(@PathVariable("portfolio_id") UUID portfolio_id) {
        return freelancerService.publishPortfolio(portfolio_id);
    }

    @DeleteMapping(path = "/portfolio/{portfolio_id}")
    public ResponseEntity<Object> deletePortfolio(@PathVariable("portfolio_id") UUID portfolio_id) {
        boolean updated =freelancerService.deletePortfolio(portfolio_id);
        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer portfolio deleted successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Portfolio not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/portfolio/{portfolio_id}")
    public ResponseEntity<Object> updatePortfolio(@PathVariable("portfolio_id") UUID portfolio_id,@RequestBody PortfolioRequestDto portfolioRequestDto) {
        portfolioRequestDto.setPortfolio_id(portfolio_id);
        boolean updated =freelancerService.updatePortfolio(portfolioRequestDto);
        if(updated){
            return ResponseHandler.generateGeneralResponse("Freelancer portfolio updated successfully", HttpStatus.OK);
        }
        return ResponseHandler.generateErrorResponse("Portfolio not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/service/{freelancer_id}")
    public ResponseEntity<Object> addService(@PathVariable("freelancer_id") UUID freelancer_id,@RequestBody ServiceDto serviceDto) {
        serviceDto.setFreelancer_id(freelancer_id);
        return freelancerService.addService(serviceDto);
    }

    @PatchMapping(path = "/service/unpublish/{service_id}")
    public ResponseEntity<Object> unpublishService(@PathVariable("service_id") UUID service_id) {
        return freelancerService.unpublishService(service_id);
    }

    @PatchMapping(path = "/service/publish/{service_id}")
    public ResponseEntity<Object> publishService(@PathVariable("service_id") UUID service_id) {
        return freelancerService.publishService(service_id);
    }

    @DeleteMapping(path = "/service/{service_id}")
    public ResponseEntity<Object> deleteService(@PathVariable("service_id") UUID service_id) {
        return freelancerService.deleteService(service_id);
    }

    @PatchMapping(path = "/{freelancer_id}/service/{service_id}")
    public ResponseEntity<Object> updateService(@PathVariable("service_id") UUID service_id,@PathVariable("freelancer_id") UUID freelancer_id,@RequestBody ServiceDto serviceDto) {
        serviceDto.setService_id(service_id);
        serviceDto.setFreelancer_id(freelancer_id);
        return freelancerService.updateService(serviceDto);
    }

    @PostMapping(path = "/dedicated_resource/{freelancer_id}")
    public ResponseEntity<Object> addDedicatedResource(@PathVariable("freelancer_id") UUID freelancer_id, @RequestBody ResourceDto resourceDto) {
        resourceDto.setFreelancer_id(freelancer_id);
        return freelancerService.addDedicatedResource(resourceDto);
    }

    @PatchMapping(path = "/dedicated_resource/unpublish/{resource_id}")
    public ResponseEntity<Object> unpublishDedicatedResource(@PathVariable("resource_id") UUID resource_id) {
        return freelancerService.unpublishDedicatedResource(resource_id);
    }

    @PatchMapping(path = "/dedicated_resource/publish/{resource_id}")
    public ResponseEntity<Object> publishDedicatedResource(@PathVariable("resource_id") UUID resource_id) {
        return freelancerService.publishDedicatedResource(resource_id);
    }

    @DeleteMapping(path = "/dedicated_resource/{resource_id}")
    public ResponseEntity<Object> deleteDedicatedResource(@PathVariable("resource_id") UUID resource_id) {
        return freelancerService.deleteDedicatedResource(resource_id);
    }

    @PatchMapping(path = "/{freelancer_id}/dedicated_resource/{resource_id}")
    public ResponseEntity<Object> updateDedicatedResource(@PathVariable("resource_id") UUID resource_id,@PathVariable("freelancer_id") UUID freelancer_id,@RequestBody ResourceDto resourceDto) {
        resourceDto.setResource_id(resource_id);
        resourceDto.setFreelancer_id(freelancer_id);
        return freelancerService.updateDedicatedResource(resourceDto);
    }

//    @PostMapping(path = "/{freelancer_id}/quote/{job_id}")
//    public ResponseEntity<Object> addQuote(@PathVariable("freelancer_id") UUID freelancer_id,@PathVariable("job_id") UUID job_id,@RequestBody Quote quote) {
//        quote.setFreelancer_id(freelancer_id);
//        quote.setJob_id(job_id);
//        return freelancerService.addQuote(quote);
//    }

    @GetMapping(path = "/portfolio/{portfolio_id}")
    public ResponseEntity<Object> getPortfolio(@PathVariable("portfolio_id") UUID portfolio_id) {
        return freelancerService.getPortfolio(portfolio_id);
    }

    @GetMapping(path = "/portfolio/all/{freelancer_id}")
    public ResponseEntity<Object> getAllFreelancerPortfolios(@PathVariable("freelancer_id") UUID freelancer_id) {
        return freelancerService.getAllFreelancerPortfolios(freelancer_id);
    }

    @GetMapping(path = "/service/{service_id}")
    public ResponseEntity<Object> getService(@PathVariable("service_id") UUID service_id) {
        return freelancerService.getService(service_id);
    }

    @GetMapping(path = "/service/all/{freelancer_id}")
    public ResponseEntity<Object> getAllFreelancerServices(@PathVariable("freelancer_id") UUID freelancer_id) {
        return freelancerService.getAllFreelancerServices(freelancer_id);
    }

    @GetMapping(path = "/dedicated_resource/{resource_id}")
    public ResponseEntity<Object> getDedicatedResource(@PathVariable("resource_id") UUID resource_id) {
        return freelancerService.getDedicatedResource(resource_id);
    }

    @GetMapping(path = "/dedicated_resource/all/{freelancer_id}")
    public ResponseEntity<Object> getAllFreelancerDedicatedResources(@PathVariable("freelancer_id") UUID freelancer_id) {
        return freelancerService.getAllFreelancerDedicatedResources(freelancer_id);
    }

    @PostMapping(path = "{freelancer_id}/featured_team_members")
    public ResponseEntity<Object> addFeaturedTeamMembers(@PathVariable("freelancer_id") UUID freelancer_id, @RequestBody AddFeaturedTeamMembersRequestDto addFeaturedTeamMembersRequestDto) {
        addFeaturedTeamMembersRequestDto.setFreelancer_id(freelancer_id);
        return freelancerService.addFeaturedTeamMembers(addFeaturedTeamMembersRequestDto);
    }

    @DeleteMapping(path = "/team_member/{team_member_id}")
    public ResponseEntity<Object> deleteTeamMember(@PathVariable("team_member_id") UUID team_member_id) {
        return freelancerService.deleteTeamMember(team_member_id);
    }
}
