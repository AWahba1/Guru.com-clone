package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.user_type_enum;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;

    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
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

}
