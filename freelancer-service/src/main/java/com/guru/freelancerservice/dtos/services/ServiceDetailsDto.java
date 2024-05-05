package com.guru.freelancerservice.dtos.services;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.UUID;

@JsonPropertyOrder({
        "service_id", "freelancer_id", "service_title", "service_description", "service_rate", "minimum_budget", "service_thumbnail", "service_views", "service_skills", "portfolio_id"
})
public interface ServiceDetailsDto {
    UUID getService_id();
    UUID getFreelancer_id();
    String getService_title();
    String getService_description();
    BigDecimal getService_rate();
    BigDecimal getMinimum_budget();
    String getService_thumbnail();
    int getService_views();
    String[] getService_skills();
    UUID[] getPortfolio_ids();
}
