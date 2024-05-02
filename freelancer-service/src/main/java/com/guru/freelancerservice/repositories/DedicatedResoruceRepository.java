package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.models.DedicatedResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.math.BigDecimal;
import java.util.UUID;

public interface DedicatedResoruceRepository extends JpaRepository<DedicatedResource, UUID> {


    @Procedure(name = "add_dedicated_resource")
    void add_dedicated_resource(UUID _freelancer_id, String resource_name, String resource_title, String resource_summary, String[] resource_skills, BigDecimal resource_rate, String minimum_duration, String resource_image, UUID[] portfolio_ids);

    @Procedure(name = "unpublish_dedicated_resource")
    void unpublish_dedicated_resource(UUID resource_id);

    @Procedure(name = "delete_dedicated_resource")
    void delete_dedicated_resource(UUID resource_id);

    @Procedure(name = "update_dedicated_resource")
    void update_dedicated_resource(UUID resource_id, String resource_name, String resource_title, String resource_summary, String[] resource_skills, BigDecimal resource_rate, String minimum_duration, String resource_image, UUID[] portfolio_ids);

}
