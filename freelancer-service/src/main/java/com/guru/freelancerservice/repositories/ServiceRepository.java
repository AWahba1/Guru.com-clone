package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.models.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.math.BigDecimal;
import java.util.UUID;

public interface ServiceRepository  extends JpaRepository<ServiceModel, UUID> {
//    IN freelancer_id uuid,
//    IN service_title varchar(255),
//    IN service_description varchar(5000),
//    IN service_skills text[],
//    IN service_rate decimal,
//    IN minimum_budget decimal,
//    IN service_thumbnail varchar(255),
//    IN portfolio_id uuid[]

    @Procedure(name = "add_service")
    void add_service(UUID freelancer_id, String title, String description, String[] skills, BigDecimal rate, BigDecimal minimum_budget, String thumbnail, UUID[] portfolio_id);

    @Procedure(name="unpublish_service")
    void unpublish_service(UUID service_id);

    @Procedure(name="delete_service")
    void delete_service(UUID service_id);

    @Procedure(name="update_service")
    void update_service(UUID service_id, String title, String description, String[] skills, BigDecimal rate, BigDecimal minimum_budget, String thumbnail, UUID[] portfolio_id);
}
