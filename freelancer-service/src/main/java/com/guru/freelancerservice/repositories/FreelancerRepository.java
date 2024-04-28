package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.models.Freelancer;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FreelancerRepository extends JpaRepository<Freelancer, UUID> {

    @Query(value = "select * from get_freelancer_profile(?1)", nativeQuery = true)
    List<Object[]> getFreelancerProfile(UUID _freelancer_id);


}






