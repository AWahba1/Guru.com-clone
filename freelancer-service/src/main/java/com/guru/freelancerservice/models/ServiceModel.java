package com.guru.freelancerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

//CREATE TABLE services (
//        service_id UUID PRIMARY KEY,
//        freelancer_id UUID,
//        service_title VARCHAR(255),
//service_description VARCHAR(5000),
//service_skills VARCHAR(255),
//service_rate DECIMAL,
//minimum_budget DECIMAL,
//service_thumbnail VARCHAR(255),
//service_views INT DEFAULT 0,
//FOREIGN KEY (freelancer_id) REFERENCES freelancers(freelancer_id) ON DELETE CASCADE
//);

@Entity
@Table(name = "services")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceModel {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID service_id;
    private UUID freelancer_id;
    private String service_title;
    private String service_description;
    private String service_skills;
    private BigDecimal service_rate;
    private BigDecimal minimum_budget;
    private String service_thumbnail;
    private int service_views;
    private boolean is_draft;
}
