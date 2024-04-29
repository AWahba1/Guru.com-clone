package com.guru.freelancerservice.controller;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
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

    @Autowired
    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllFreelancers(){
        List<Freelancer> freelancers = freelancerService.getAllFreelancers();
        return ResponseHandler.generateResponse("Freelancers retrieved successfully", HttpStatus.OK, freelancers, freelancers.size());
    }

    @GetMapping(path = "/{freelancer_id}")
    public ResponseEntity<Object> getFreelancer(@PathVariable("freelancer_id") UUID freeLancer_id) {
        Freelancer freelancer=freelancerService.getFreelancer(freeLancer_id);
        if(freelancer==null){
            return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateResponse("Freelancer fetched successfully", HttpStatus.OK, freelancer, 1);
    }

    @GetMapping(path = "/profile/{freelancer_id}")
    public ResponseEntity<Object> getFreelancerProfile(@PathVariable("freelancer_id") UUID freeLancer_id) {
        FreelancerProfileDto returnedProfile=freelancerService.getFreelancerProfile(freeLancer_id);
        if(returnedProfile==null){
            return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateResponse("Freelancer profile fetched successfully", HttpStatus.OK, returnedProfile, 1);
    }






}
