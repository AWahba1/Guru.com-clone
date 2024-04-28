package com.guru.freelancerservice.controller;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.testDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.guru.freelancerservice.response.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {
    private final FreelancerRepository freelancerRepository;
    FreelancerService freelancerService;

    @Autowired
    public FreelancerController(FreelancerService freelancerService, FreelancerRepository freelancerRepository) {
        this.freelancerService = freelancerService;
        this.freelancerRepository = freelancerRepository;
    }

    @GetMapping(path = "/profile/{freelancer_id}")
    public ResponseEntity<Object> getFreelancerProfile(@PathVariable("freelancer_id") UUID freeLancer_id) {
        FreelancerProfileDto returnedProfile=freelancerService.getFreelancerProfile(freeLancer_id);
        if(returnedProfile==null){
            return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateResponse("Freelancer profile fetched successfully", HttpStatus.OK, returnedProfile, 1);
    }

    @PostMapping
    public ResponseEntity<Object> addFreelancer (@RequestBody Freelancer freelancer){
        Freelancer newFreelancer = freelancerRepository.save(freelancer);
        return ResponseHandler.generateResponse("Freelancer added successfully", HttpStatus.CREATED, newFreelancer, 1);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFreelancers(){
        return ResponseHandler.generateResponse("Freelancers retrieved successfully", HttpStatus.OK, freelancerRepository.findAll(), freelancerRepository.findAll().size());
    }

    @GetMapping(path = "/name")
    public ResponseEntity<Object> getFreelancerName(){
        List<Object[]> names= freelancerRepository.getFreelancerName();
        List<testDto> test=new ArrayList<testDto>();
        for(Object name:names){
            testDto temp=new testDto();
            temp.setFreelancer_name(((Object[]) name)[0].toString());
            temp.setProfile_views(Integer.parseInt(((Object[]) name)[1].toString()));
            test.add(temp);
        }
        return ResponseHandler.generateResponse("Freelancer names retrieved successfully", HttpStatus.OK, test, test.size());
    }

}
