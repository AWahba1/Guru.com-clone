package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FreelancerRepository extends JpaRepository<Freelancer, UUID> {

@Query(value = "select * from get_freelancer_profile(:_freelancer_id)", nativeQuery = true)
FreelancerProfileDto getFreelancerProfile(UUID freelancer_id);
}
