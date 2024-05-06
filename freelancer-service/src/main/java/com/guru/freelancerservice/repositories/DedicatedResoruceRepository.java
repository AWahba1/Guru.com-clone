package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.resources.ResourceDetailsDto;
import com.guru.freelancerservice.dtos.resources.ResourceListViewDto;
import com.guru.freelancerservice.dtos.services.ServiceDetailsDto;
import com.guru.freelancerservice.dtos.services.ServiceListViewDto;
import com.guru.freelancerservice.models.DedicatedResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DedicatedResoruceRepository extends JpaRepository<DedicatedResource, UUID> {


    @Procedure(name = "add_dedicated_resource")
    void add_dedicated_resource(UUID _freelancer_id, String resource_name, String resource_title, String resource_summary, UUID[] resource_skills, BigDecimal resource_rate, String minimum_duration, String resource_image, UUID[] portfolio_ids);

    @Procedure(name = "unpublish_dedicated_resource")
    void unpublish_dedicated_resource(UUID resource_id);

    @Procedure(name = "publish_dedicated_resource")
    void publish_dedicated_resource(UUID resource_id);

    @Procedure(name = "delete_dedicated_resource")
    void delete_dedicated_resource(UUID resource_id);

    @Procedure(name = "update_dedicated_resource")
    void update_dedicated_resource(UUID resource_id, String resource_name, String resource_title, String resource_summary, UUID[] resource_skills, BigDecimal resource_rate, String minimum_duration, String resource_image, UUID[] portfolio_ids);

    @Query(value = "SELECT * FROM get_resource_details(?1)", nativeQuery = true)
    List<ResourceDetailsDto> get_resource_details(UUID resource_id);

    @Query(value = "SELECT * FROM get_all_freelancer_dedicated_resources(?1)", nativeQuery = true)
    List<ResourceListViewDto> get_all_freelancer_dedicated_resources(UUID freelancer_id);
}
