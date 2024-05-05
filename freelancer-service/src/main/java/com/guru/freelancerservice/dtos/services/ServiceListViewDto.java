package com.guru.freelancerservice.dtos.services;

//service_id uuid,
//service_title varchar(255),
//service_description varchar(5000),
//service_skills varchar(255) ARRAY,
//service_rate decimal,
//minimum_budget decimal,
//service_thumbnail varchar(255)

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.UUID;

@JsonPropertyOrder({
        "service_id", "service_title", "service_description", "service_skills", "service_rate", "minimum_budget", "service_thumbnail"
})
public interface ServiceListViewDto {
    UUID getService_id();
    String getService_title();
    String getService_description();
    String[] getService_skills();
    BigDecimal getService_rate();
    BigDecimal getMinimum_budget();
    String getService_thumbnail();
}
