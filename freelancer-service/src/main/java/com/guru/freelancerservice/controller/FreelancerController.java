package com.guru.freelancerservice.controller;

import com.guru.freelancerservice.repositories.FreelancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {
    FreelancerRepository freelancerRepository;

    @Autowired
    public FreelancerController(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    @GetMapping(value = "/profile", path = "{freelancer_id}")
    public void getFreelancerProfile() {

    }


}
