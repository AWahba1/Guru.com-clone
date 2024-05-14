package com.guru.freelancerservice.dtos.resources;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.UUID;

//resource_id UUID,
//freelancer_id UUID,
//resource_name VARCHAR(255),
//resource_title VARCHAR(255),
//resource_summary VARCHAR(3000),
//resource_skills varchar(255) ARRAY,
//resource_rate DECIMAL,
//minimum_duration resource_duration_enum,
//resource_image VARCHAR(255),
//resource_views INT,
//portfolio_id UUID,
//portfolio_title VARCHAR(255),
//portfolio_cover_image_url VARCHAR(255)

@JsonPropertyOrder({
        "resource_id", "freelancer_id", "resource_name", "resource_title", "resource_summary", "resource_rate", "minimum_duration", "resource_image", "resource_views", "resource_skills", "portfolio_ids"
})
public interface ResourceDetailsDto {
    UUID getResource_id();
    UUID getFreelancer_id();
    String getResource_name();
    String getResource_title();
    String getResource_summary();
    BigDecimal getResource_rate();
    String getMinimum_duration();
    String getResource_image();
    int getResource_views();
    String[] getResource_skills();
    UUID[] getPortfolio_ids();
}
